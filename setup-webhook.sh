#!/bin/bash

# Ensure the script exits on any error
set -e

if [ -z "$PACT_BROKER_TOKEN" ]; then
    echo "Error: PACT_BROKER_TOKEN environment variable is not set."
    echo "Usage: PACT_BROKER_TOKEN=<your_token> ./setup-webhook.sh"
    exit 1
fi

export PACT_BROKER_BASE_URL="https://xxxldigital.pactflow.io"
export PACTICIPANT="hype-stan-v3-client"
export GITHUB_WEBHOOK_UUID="7217571b-d326-4dae-b9e1-53a867f7b5a9"
export PACT_CLI="docker run --rm -v ${PWD}:${PWD} -e PACT_BROKER_BASE_URL -e PACT_BROKER_TOKEN pactfoundation/pact-cli:latest"

echo "========================================="
echo "1. Creating/Updating GitHub Webhook in PactFlow..."
echo "========================================="
$PACT_CLI broker create-or-update-webhook \
    'https://api.github.com/repos/luciancrasovan/example-bi-directional-consumer-wiremock/statuses/${pactbroker.consumerVersionNumber}' \
    --header 'Content-Type: application/json' 'Accept: application/vnd.github.v3+json' 'Authorization: token ${user.githubCommitStatusToken}' \
    --request POST \
    --data @${PWD}/pactflow/github-commit-status-webhook.json \
    --uuid ${GITHUB_WEBHOOK_UUID} \
    --consumer ${PACTICIPANT} \
    --contract-published \
    --provider-verification-published \
    --description "Github commit status webhook for ${PACTICIPANT}"

echo ""
echo "========================================="
echo "2. Testing Webhook Execution..."
echo "========================================="
curl -v -X POST ${PACT_BROKER_BASE_URL}/webhooks/${GITHUB_WEBHOOK_UUID}/execute -H "Authorization: Bearer ${PACT_BROKER_TOKEN}"

echo ""
echo "========================================="
echo "Webhook setup and test complete!"
echo "========================================="
