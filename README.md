# test-infra

## Test observability platform

### Spec

- ✅ Should be able to list all the tests in the suite
- ✅ Should be able to identify owners for a given test in the suite
- ✅ Should be able to tag each test to a functional area
- ✅ Should be able to get metrics on how many tests belong to a given area
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
