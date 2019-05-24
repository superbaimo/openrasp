--TEST--
hook MongoDB\Driver\BulkWrite::update
--SKIPIF--
<?php
$plugin = <<<EOF
plugin.register('mongo', params => {
    assert(params.query == '{"likes":100}')
    assert(params.server == 'mongodb')
    assert(params.class.endsWith('Bulkwrite'))
    assert(params.method == 'update')
    return block
})
EOF;
$conf = <<<CONF
security.enforce_policy: true
CONF;
include(__DIR__.'/../skipif.inc');
if (!extension_loaded("mongodb")) die("Skipped: mongodb extension required.");
?>
--INI--
openrasp.root_dir=/tmp/openrasp
--FILE--
<?php
$manager = new MongoDB\Driver\Manager("mongodb://openrasp:rasp#2019@localhost:27017/test");
$bulk = new MongoDB\Driver\BulkWrite;
$bulk->update(['likes' => 100], ['likes' => 200]);
?>
--EXPECTREGEX--
<\/script><script>location.href="http[s]?:\/\/.*?request_id=[0-9a-f]{32}"<\/script>