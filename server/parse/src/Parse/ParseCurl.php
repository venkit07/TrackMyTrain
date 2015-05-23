<?php

namespace Parse;

/**
 * Class ParseCurl
 *
 * This class w/ function bundle overloads the basic php curl_* functions in the ParseHelper-Namespace.
 *
 * The main reason for that was to inject CURLOPT_SSL_VERIFYHOST and CURLOPT_SSL_VERIFYPEER options
 * to prevent ssl certificate errors. Second, there is the possibility to hook the curl_exec call
 * with a given exec-wrapper function, for e.g. build logging, progress monitoring, caching...
 *
 * To inject curl_setopt by default on all curl resources created via curl_init use:
 *   - ParseCurl::init_setopt( $option, $value ) sets options immediatly after generation by curl_init
 *   - ParseCurl::exec_setopt( $option, $value ) sets options immediatly before curl_exec runs
 *
 * @package Parse
 */

use Parse\ParseCurl\ExecInterface;

class ParseCurl
{

    public static $curl_errno = array();
    public static $curl_error = array();
    public static $curl_setopt = array();
    public static $curl_getinfo = array();

    public static $init_setopt = array();
    public static $exec_setopt = array();
    public static $exec;

    public static $info_option
        = array(
            'url'                     => CURLINFO_EFFECTIVE_URL,
            'content_type'            => CURLINFO_CONTENT_TYPE,
            'http_code'               => CURLINFO_HTTP_CODE,
            'header_size'             => CURLINFO_HEADER_SIZE,
            'request_size'            => CURLINFO_REQUEST_SIZE,
            'filetime'                => CURLINFO_FILETIME,
            'ssl_verify_result'       => CURLINFO_SSL_VERIFYRESULT,
            'redirect_count'          => CURLINFO_REDIRECT_COUNT,
            'total_time'              => CURLINFO_TOTAL_TIME,
            'namelookup_time'         => CURLINFO_NAMELOOKUP_TIME,
            'connect_time'            => CURLINFO_CONNECT_TIME,
            'pretransfer_time'        => CURLINFO_PRETRANSFER_TIME,
            'size_upload'             => CURLINFO_SIZE_UPLOAD,
            'size_download'           => CURLINFO_SIZE_DOWNLOAD,
            'speed_download'          => CURLINFO_SPEED_DOWNLOAD,
            'speed_upload'            => CURLINFO_SPEED_UPLOAD,
            'download_content_length' => CURLINFO_CONTENT_LENGTH_DOWNLOAD,
            'upload_content_length'   => CURLINFO_CONTENT_LENGTH_UPLOAD,
            'starttransfer_time'      => CURLINFO_STARTTRANSFER_TIME,
            'redirect_time'           => CURLINFO_REDIRECT_TIME
        );

    /**
     * Set global curl option for all curl resources immediatly before curl_exec runs
     *
     * @param $option
     * @param $value
     */
    public static function exec_setopt( $option, $value )
    {
        ParseCurl::$exec_setopt[$option] = $value;
    }

    /**
     * Set global curl option for all curl resources immediatly after generation by curl_init
     *
     * @param $option
     * @param $value
     */
    public static function init_setopt( $option, $value )
    {
        ParseCurl::$init_setopt[$option] = $value;
    }

    /**
     * Set an curl_exec wrapper function($ch, $method, $url, $options)
     *
     * @param $call
     */
    public static function exec( $call )
    {
        if (is_object( $call ) && $call instanceof ExecInterface) {
            ParseCurl::$exec = $call;
        } elseif (is_callable( $call )) {
            ParseCurl::$exec = $call;
        }
    }

    /**
     * Helper to invoke overloaded parse/curl_errno function from outside parse namespace
     *
     * @param $ch
     *
     * @return int
     */
    public static function curl_errno( $ch )
    {
        return curl_errno( $ch );
    }

    /**
     * Inject parse/curl_errno with your own errno value
     *
     * @param $ch
     * @param $value
     *
     * @return mixed
     */
    public static function curl_errno_set( $ch, $value )
    {
        return ParseCurl::$curl_errno[(string)$ch] = $value;
    }

    /**
     * Helper to invoke overloaded parse/curl_error function from outside parse namespace
     *
     * @param $ch
     *
     * @return string
     */
    public static function curl_error( $ch )
    {
        return curl_error( $ch );
    }

    /**
     * Inject parse/curl_errno with your own error value
     *
     * @param $ch
     * @param $value
     *
     * @return mixed
     */
    public static function curl_error_set( $ch, $value )
    {
        return ParseCurl::$curl_error[(string)$ch] = $value;
    }

    /**
     * Helper to invoke overloaded parse/curl_getinfo function from outside parse namespace
     *
     * @param     $ch
     * @param int $opt
     *
     * @return array|mixed
     */
    public static function curl_getinfo( $ch, $opt = 0 )
    {
        return curl_getinfo( $ch, $opt );
    }

    /**
     * Inject parse/curl_getinfo with your own info value
     *
     * @param $ch
     * @param $info
     * @param $value
     *
     * @return $value
     */
    public static function curl_setinfo( $ch, $info, $value )
    {
        return curl_setinfo( $ch, $info, $value );
    }

    /**
     * Inject parse/curl_getinfo with your own info values
     *
     * @param $ch
     * @param $infos
     *
     * @return $infos
     */
    public static function curl_setinfo_array( $ch, $infos )
    {
        return curl_setinfo_array( $ch, $infos );
    }

    /**
     * Execute a curl_exec wrapper given in $exec parameter or calls original php curl_exec function
     *
     * @param      $ch
     * @param null $method
     * @param null $url
     * @param null $options
     * @param null $exec
     *
     * @return mixed
     */
    public static function curl_exec( $ch, $method = null, $url = null, $options = null, $exec = null )
    {
        if (is_object( $exec ) && $exec instanceof ExecInterface) {
            return $exec->exec( $ch, $method, $url, $options );
        } elseif (is_callable( $exec )) {
            return $exec( $ch, $method, $url, $options );
        } else {
            return \curl_exec( $ch );
        }
    }
}

/**
 * Overload curl_init function
 *
 * Setup curl_setopt and curl_getinfo temporary storage hash-array
 * and set global registered options by ParseCurl::init_setopt(..)
 *
 * @param null $url
 *
 * @return resource
 */
function curl_init( $url = null )
{
    // Call php curl_init
    $ch = \curl_init( $url );

    // Setup the $curl_setopt option storage
    if ($url) {
        ParseCurl::$curl_setopt[(string)$ch] = array( CURLOPT_URL => $url );
    } else {
        ParseCurl::$curl_setopt[(string)$ch] = array();
    }

    // Setup $curl_getinfo, $curl_errno, $curl_error storage
    ParseCurl::$curl_getinfo[(string)$ch] = array();
    ParseCurl::$curl_errno[(string)$ch] = array();
    ParseCurl::$curl_error[(string)$ch] = array();

    // Set global registered curl_init options
    foreach (ParseCurl::$init_setopt as $option => $value) {
        curl_setopt( $ch, $option, $value );
    }

    return $ch;
}

/**
 * Overload curl_setopt function
 *
 * Store the option in temporary options storage an set php curl_setopt option
 *
 * @param $ch
 * @param $option
 * @param $value
 *
 * @return bool
 */
function curl_setopt( $ch, $option, $value )
{
    return \curl_setopt( $ch, $option, ParseCurl::$curl_setopt[(string)$ch][$option] = $value );
}

/**
 * Overload curl_setopt_array function
 *
 * @see ParseCurl curl_setop
 *
 * @param $ch
 * @param $options
 *
 * @return bool
 */
function curl_setopt_array( $ch, $options )
{
    foreach ($options as $option => $value) {
        if (curl_setopt( $ch, $option, $value ) == false) {
            return false;
        }
    }

    return true;
}

/**
 * Overload curl_getinfo function
 *
 * Store retrieved info values into temporary info storage array
 * and/or get info from temporary info storage array set by curl_setinfo
 *
 * @param     $ch
 * @param int $opt
 *
 * @return array|mixed
 */
function curl_getinfo( $ch, $opt = 0 )
{
    if ($opt) {
        return array_key_exists( $opt, ParseCurl::$curl_getinfo[(string)$ch] )
            ? ParseCurl::$curl_getinfo[(string)$ch][$opt]
            : ParseCurl::$curl_getinfo[(string)$ch][$opt] = \curl_getinfo( $ch, $opt );
    } else {
        $infos = array();
        foreach (ParseCurl::$info_option as $name => $info) {
            $infos[$name] = curl_getinfo( $ch, $info );
        }

        return $infos;
    }
}

/**
 * Additional curl_setinfo function, to set info programmatically.
 *
 * @param $ch
 * @param $opt
 * @param $value
 *
 * @return $value
 */
function curl_setinfo( $ch, $info, $value )
{
    return ParseCurl::$curl_getinfo[(string)$ch][$info] = $value;
}

/**
 * Additional curl_setinfo_array function, to set infos by name programmatically.
 *
 * @param $ch
 * @param $values
 *
 * @return $value
 */
function curl_setinfo_array( $ch, $infos )
{
    foreach ($infos as $name => $value) {
        if (array_key_exists( $name, ParseCurl::$info_option )) {
            ParseCurl::$curl_getinfo[(string)$ch][$info = ParseCurl::$info_option[$name]] = $value;
        }
    }
    return $infos;
}

/**
 * Get curl_errno, eventually previously set by curl_setinfo($ch,'curl_errno',$errno)
 *
 * @param $ch
 *
 * @return int
 */
function curl_errno( $ch )
{
    return ParseCurl::$curl_errno[(string)$ch] ? : ParseCurl::$curl_errno[(string)$ch] = \curl_errno( $ch );
}

/**
 * Get curl_errrot, eventually previously set by curl_setinfo($ch,'curl_error',$error)
 *
 * @param $ch
 *
 * @return string
 */
function curl_error( $ch )
{
    return ParseCurl::$curl_error[(string)$ch] ? : ParseCurl::$curl_error[(string)$ch] = \curl_errno( $ch );
}

/**
 * Overload curl_exec function
 *
 * Set global registered curl options set by ParseCurl::exec_setopt(..)
 * Call curl_exec wrapper function if registered by ParseCurl::exec(..)
 *
 * @param $ch
 *
 * @return mixed
 */
function curl_exec( $ch )
{
    // Set curl_options
    foreach (ParseCurl::$exec_setopt as $option => $value) {
        curl_setopt( $ch, $option, $value );
    }

    // Call exec wrapper function if registerd
    if (ParseCurl::$exec) {
        // get curl options ffrom tempory storage
        $options = ParseCurl::$curl_setopt[(string)$ch];
        // get curl url
        $url = $options[CURLOPT_URL];
        // discover the curl HTTP method
        if (isset($options[CURLOPT_CUSTOMREQUEST]) && $options[CURLOPT_CUSTOMREQUEST]) {
            $method = $options[CURLOPT_CUSTOMREQUEST];
        } else {
            $method = 'GET';
            $curl_opt_methods = array( CURLOPT_HTTPGET => 'GET', CURLOPT_POST => 'POST', CURLOPT_PUT => 'PUT' );
            foreach ($curl_opt_methods as $option => $curl_opt_method) {
                if (isset($options[$option]) && $options[$option]) {
                    $method = $curl_opt_method;
                }
            }
        }

        // call curl_exec with user exec wrapper function
        return ParseCurl::curl_exec( $ch, $method, $url, $options, ParseCurl::$exec );
    } else {
        // or curl_exec withut wrapper
        return ParseCurl::curl_exec( $ch );
    }
}

function curl_close( $ch )
{
    // destroy temporary storages
    unset(ParseCurl::$curl_errno[(string)$ch]);
    unset(ParseCurl::$curl_error[(string)$ch]);
    unset(ParseCurl::$curl_setopt[(string)$ch]);
    unset(ParseCurl::$curl_getinfo[(string)$ch]);

    // close curl resource
    return \curl_close( $ch );
}