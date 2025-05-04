# Prompts

## Get failure stack trace

As a ReportPortal java programmer, you can follow below steps to get the stack trace for failed items, I have listed the cURL and a sample response. Please update ReportPortalClient inline with below steps in order to fetch the stack traces for failed cases and proceed further

1. Get Latest launch id

```
curl --location 'http://localhost:8080/api/v1/test_infra/launch/latest' \
--header 'Accept: application/json' \
--header 'Authorization: Bearer test-infra_iKPprGYfS96dyOQDD1tJfi7cRGhSu6zMrUolU9olbfnOdCrb8qWDE4O8CvxloIPc'
```

Response

```
{
    "content": [
        {
            "owner": "superadmin",
            "id": 35,
            "uuid": "32a2ad18-8e4e-46d0-b601-aa82f141fe1c",
            "name": "reqres",
            "number": 13,
            "startTime": "2025-05-02T16:43:21.360Z",
            "endTime": "2025-05-02T16:43:33.153Z",
            "lastModified": "2025-05-02T16:43:33.166849Z",
            "status": "FAILED",
            "statistics": {
                "executions": {
                    "total": 21,
                    "failed": 15,
                    "passed": 6
                },
                "defects": {
                    "to_investigate": {
                        "total": 15,
                        "ti001": 15
                    }
                }
            },
            "attributes": [
                {
                    "key": "group",
                    "value": "test_infra"
                },
                {
                    "key": "test_type",
                    "value": "backend"
                }
            ],
            "mode": "DEFAULT",
            "analysing": [],
            "approximateDuration": 11.9532,
            "hasRetries": false,
            "rerun": false,
            "metadata": {
                "rp.cluster.lastRun": "1746204213951"
            }
        }
    ],
    "page": {
        "number": 1,
        "size": 20,
        "totalElements": 1,
        "totalPages": 1
    }
}
```

2. Get Launch using id

```
curl --location 'http://localhost:8080/api/v1/test_infra/launch/34' \
--header 'Accept: application/json' \
--header 'Authorization: Bearer test-infra_iKPprGYfS96dyOQDD1tJfi7cRGhSu6zMrUolU9olbfnOdCrb8qWDE4O8CvxloIPc'
```

```
{
    "owner": "superadmin",
    "id": 34,
    "uuid": "69f4e742-b9a3-4fc6-9200-5e6c8eb62ea5",
    "name": "reqres",
    "number": 12,
    "startTime": "2025-05-02T16:13:11.497Z",
    "endTime": "2025-05-02T16:13:23.358Z",
    "lastModified": "2025-05-02T16:13:23.372563Z",
    "status": "FAILED",
    "statistics": {
        "executions": {
            "total": 21,
            "passed": 6,
            "failed": 15
        },
        "defects": {
            "to_investigate": {
                "total": 15,
                "ti001": 15
            }
        }
    },
    "attributes": [
        {
            "key": "group",
            "value": "test_infra"
        },
        {
            "key": "test_type",
            "value": "backend"
        }
    ],
    "mode": "DEFAULT",
    "analysing": [],
    "approximateDuration": 11.295,
    "hasRetries": false,
    "rerun": false,
    "metadata": {
        "rp.cluster.lastRun": "1746202404257"
    },
    "retentionPolicy": "REGULAR"
}
```

3. Get failed test items

```
curl --location 'http://localhost:8080/api/v1/test_infra/item/v2?filter.in.type=STEP&filter.in.status=FAILED%2CINTERRUPTED&providerType=launch&launchId=35' \
--header 'Accept: application/json, text/plain, */*' \
--header 'Authorization: Bearer test-infra_iKPprGYfS96dyOQDD1tJfi7cRGhSu6zMrUolU9olbfnOdCrb8qWDE4O8CvxloIPc'
```

```
{
    "content": [
        {
            "id": 634,
            "uuid": "935446e9-8efc-46c5-b662-a914f8b840d8",
            "name": "testLoginUnsuccessful",
            "codeRef": "io.automationhacks.testinfra.reqres.login.ReqResLoginTest.testLoginUnsuccessful",
            "description": "Error: \njava.lang.AssertionError: 1 expectation failed.\nExpected status code <400> but was <401>.\n\n\tat java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:77)\n\tat java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)\n\tat java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:499)\n\tat java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:480)\n\tat org.codehaus.groovy.reflection.CachedConstructor.invoke(CachedConstructor.java:73)\n\tat org.codehaus.groovy.runtime.callsite.ConstructorSite$ConstructorSiteNoUnwrapNoCoerce.callConstructor(ConstructorSite.java:108)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallConstructor(CallSiteArray.java:57)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:263)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:277)\n\tat io.restassured.internal.ResponseSpecificationImpl$HamcrestAssertionClosure.validate(ResponseSpecificationImpl.groovy:512)\n\tat io.restassured.internal.ResponseSpecificationImpl$HamcrestAssertionClosure$validate$11.call(Unknown Source)\n\tat io.restassured.internal.ResponseSpecificationImpl.validateResponseIfRequired(ResponseSpecificationImpl.groovy:696)\n\tat io.restassured.internal.ResponseSpecificationImpl.this$2$validateResponseIfRequired(ResponseSpecificationImpl.groovy)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:568)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMet",
            "parameters": [],
            "attributes": [
                {
                    "key": "team",
                    "value": "onboarding"
                }
            ],
            "type": "STEP",
            "startTime": "2025-05-02T16:43:22.038Z",
            "endTime": "2025-05-02T16:43:23.160Z",
            "status": "FAILED",
            "statistics": {
                "executions": {
                    "total": 1,
                    "failed": 1
                },
                "defects": {
                    "to_investigate": {
                        "total": 1,
                        "ti001": 1
                    }
                }
            },
            "parent": 628,
            "pathNames": {
                "launchPathName": {
                    "name": "reqres",
                    "number": 13
                },
                "itemPaths": [
                    {
                        "id": 627,
                        "name": "Gradle suite"
                    },
                    {
                        "id": 628,
                        "name": "Gradle test"
                    }
                ]
            },
            "issue": {
                "issueType": "ti001",
                "autoAnalyzed": false,
                "ignoreAnalyzer": false,
                "externalSystemIssues": []
            },
            "hasChildren": false,
            "hasStats": true,
            "launchId": 35,
            "uniqueId": "auto:4e40dc736206f3ee46fd399429c92c6c",
            "testCaseId": "io.automationhacks.testinfra.reqres.login.ReqResLoginTest.testLoginUnsuccessful",
            "testCaseHash": -1276741137,
            "patternTemplates": [
                "Status code mismatch"
            ],
            "path": "627.628.634"
        },
        {
            "id": 635,
            "uuid": "a2f2cc27-df13-4fb3-b6b5-4f7f9d8ab84b",
            "name": "testRegisterUnsuccessful",
            "codeRef": "io.automationhacks.testinfra.reqres.register.ReqResRegistrationTest.testRegisterUnsuccessful",
            "description": "Error: \njava.lang.AssertionError: 1 expectation failed.\nExpected status code <400> but was <401>.\n\n\tat java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:77)\n\tat java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)\n\tat java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:499)\n\tat java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:480)\n\tat org.codehaus.groovy.reflection.CachedConstructor.invoke(CachedConstructor.java:73)\n\tat org.codehaus.groovy.runtime.callsite.ConstructorSite$ConstructorSiteNoUnwrapNoCoerce.callConstructor(ConstructorSite.java:108)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallConstructor(CallSiteArray.java:57)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:263)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:277)\n\tat io.restassured.internal.ResponseSpecificationImpl$HamcrestAssertionClosure.validate(ResponseSpecificationImpl.groovy:512)\n\tat io.restassured.internal.ResponseSpecificationImpl$HamcrestAssertionClosure$validate$11.call(Unknown Source)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:45)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:125)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:139)\n\tat io.restassured.internal.ResponseSpecificationImpl.validateResponseIfRequired(ResponseSpecificationImpl.groovy:696)\n\tat io.restassured.internal.ResponseSpecificationImpl.this$2$validateResponseIfRequired(ResponseSpecificationImpl.groovy)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:",
            "parameters": [],
            "attributes": [
                {
                    "key": "team",
                    "value": "onboarding"
                }
            ],
            "type": "STEP",
            "startTime": "2025-05-02T16:43:22.038Z",
            "endTime": "2025-05-02T16:43:23.160Z",
            "status": "FAILED",
            "statistics": {
                "executions": {
                    "total": 1,
                    "failed": 1
                },
                "defects": {
                    "to_investigate": {
                        "total": 1,
                        "ti001": 1
                    }
                }
            },
            "parent": 628,
            "pathNames": {
                "launchPathName": {
                    "name": "reqres",
                    "number": 13
                },
                "itemPaths": [
                    {
                        "id": 627,
                        "name": "Gradle suite"
                    },
                    {
                        "id": 628,
                        "name": "Gradle test"
                    }
                ]
            },
            "issue": {
                "issueType": "ti001",
                "autoAnalyzed": false,
                "ignoreAnalyzer": false,
                "externalSystemIssues": []
            },
            "hasChildren": false,
            "hasStats": true,
            "launchId": 35,
            "uniqueId": "auto:e916cf6467d8ead3b9a5adc1fc9c8a5e",
            "testCaseId": "io.automationhacks.testinfra.reqres.register.ReqResRegistrationTest.testRegisterUnsuccessful",
            "testCaseHash": -483660129,
            "patternTemplates": [
                "Status code mismatch"
            ],
            "path": "627.628.635"
        }
    ],
    "page": {
        "number": 1,
        "size": 20,
        "totalElements": 15,
        "totalPages": 1
    }
}
```
