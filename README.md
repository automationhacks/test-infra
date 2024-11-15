# test-infra

## Test observability platform

### Spec

- ✅ Should be able to list all the tests in the suite
- ✅ Should be able to identify owners for a given test in the suite
- ✅ Should be able to tag each test to a functional area
- ✅ Should be able to get metrics on how many tests belong to a given area
- ✅ Should be able to trigger a slack summary with each individual test failure as a child message in thread
- Should be able to calculate if a test is broken consistently (via report portal)
- Should be able to calculate if a test is flaky consistently (via areport portal)

### Running tests

Run all tests

```commandline
./gradlew clean test --info
```

Run only groups belonging to a certain group

```commandline
./gradlew test -DincludedGroups=smoke --info
```

Run tests belonging to a group but excluding few

```commandline
./gradlew test -DincludedGroups=smoke -DexcludedGroups=slow --info
```

Run tests belonging to multiple groups

```commandline
./gradlew test -DincludedGroups=smoke,regression --info
```

Run tests only when they belong to both the groups specified

```commandline
./gradlew runTests -Dgroups=smoke,regression --info
```

### Check test listing dashboard

```commandline
cd build/resources/main
```

```commandline
python3 -m http.server 8000
```

Open the generated html by navigating to:

http://localhost:8000/static/index.html

### Report portal (one time setup)

Download and install docker from [docker website](https://www.docker.com/) depending on your platform and install

```commandline
brew install docker-compose
```

You can skip below if you already have docker-compose.yml file

```commandline
curl -LO https://raw.githubusercontent.com/reportportal/reportportal/master/docker-compose.yml
```

```commandline
docker-compose -p reportportal up -d --force-recreate
```

Explanation

This docker-compose command does the following:

- `-p reportportal`: Sets the project name to reportportal, which namespaces the containers, networks, and volumes
  created.
- `up`: Builds, (re)creates, and starts the services defined in the docker-compose.yml file.
- `-d`: Runs the containers in detached mode (in the background).
  `--force-recreate`: Forces a recreation of containers even if no configuration has changed.

### Setup Jenkins

This project shares an example Jenkinsfile with a pipeline to indicate how a build and test run pipeline could look like

### Test metrics in report portal

Let's first generate some test data to then organize it within report portal

```commandline
./gradlew test -DincludedGroups=identity -Drp.launch=identity_tests -Drp.attributes="group:test_infra;test_type:backend;team:identity" --info
```

```commandline
./gradlew test -DincludedGroups=onboarding -Drp.launch=onboarding_tests -Drp.attributes="group:test_infra;test_type:backend;team:onboarding" --info
```

```commandline
./gradlew test -DincludedGroups=performance -Drp.launch=performance_tests -Drp.attributes="group:test_infra;test_type:backend;team:performance" --info
```