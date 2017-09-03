#!/bin/bash

# Start nginx proxy in background
nginx &

# Start backend up in foreground
/usr/local/bin/spring-boot --server.contextPath=/api --encryption.key=${ENCRYPTION_KEY} $@
