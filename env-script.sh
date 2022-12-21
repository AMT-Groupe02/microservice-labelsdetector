#!/bin/sh

touch .env
{
  printf "ACCESS_KEY=%s\n" "$ACCESS_KEY"
  printf "SECRET_KEY=%s\n" "$SECRET_KEY"
} >> .env