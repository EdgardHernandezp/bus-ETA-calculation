# bus-ETA-calculation
Project for a event driven system where the publisher emits the current location of a bus into a message broker and the consumer is an app for the bus stops which use the location of buses to calculate and show ETA to people waiting.

# Running the tests using Colima
Set this two environment variables when executing any command which run the generated unit test:
1. DOCKER_HOST=unix://${HOME}/.colima/default/docker.sock
2. TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock
