# test-infra

## Test observability platform

### Spec

- ✅ Should be able to list all the tests in the suite
- ✅ Should be able to identify owners for a given test in the suite
- ✅ Should be able to tag each test to a functional area
- ✅ Should be able to get metrics on how many tests belong to a given area
- ✅ Should be able to trigger a slack summary with each individual test failure as a child message in thread
-
- Should be able to calculate if a test is broken consistently
- Should be able to calculate if a test is flaky consistently

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

### Report portal

```commandline
brew install docker-compose
```

```commandline
curl -LO https://raw.githubusercontent.com/reportportal/reportportal/master/docker-compose.yml
```

```commandline
docker-compose -p reportportal up -d --force-recreate
```