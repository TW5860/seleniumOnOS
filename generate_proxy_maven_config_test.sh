#!/bin/bash

TEST_NUM=1

function test() {
    diff <(./generate_proxy_maven_config.sh "$2") - \
      && echo "Test #$TEST_NUM - $1 successful" \
      || echo "Test #$TEST_NUM - $1 failed"

    TEST_NUM=$((${TEST_NUM}+1))
}

test "full example" "http://user:password@host.domain.com:8080" <<END_EXPECTED
<settings>
  <proxies>
   <proxy>
      <id>example-proxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>host.domain.com</host>
      <port>8080</port>
      <username>user</username>
      <password>password</password>
    </proxy>
  </proxies>
</settings>
END_EXPECTED

test "without username and password" "http://host.domain.com:8080" <<END_EXPECTED
<settings>
  <proxies>
   <proxy>
      <id>example-proxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>host.domain.com</host>
      <port>8080</port>
    </proxy>
  </proxies>
</settings>
END_EXPECTED