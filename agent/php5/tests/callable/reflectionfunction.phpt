--TEST--
hook reflectionfunction
--SKIPIF--
<?php
$conf = <<<CONF
callable_blacklist=["system", "exec"]
CONF;
include(__DIR__.'/../skipif.inc');
?>
--INI--
openrasp.root_dir=/tmp/openrasp
--FILE--
<?php
$func = new ReflectionFunction('system'); 
?>
--EXPECTREGEX--
<\/script><script>location.href="http[s]?:\/\/.*?request_id=[0-9a-f]{32}"<\/script>