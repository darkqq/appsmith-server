#!/bin/bash

set -e

# build cra app
REACT_APP_CLIENT_LOG_LEVEL=ERROR EXTEND_ESLINT=true craco --max-old-space-size=4096 build --config craco.build.config.js

echo "build finished"

# build storybook and move to the static folder
yarn --cwd packages/storybook build
mv -f ./packages/storybook/storybook-static ./build/storybook

