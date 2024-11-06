pipeline {
    agent {
        any {
            image 'maven:3.9.9-eclipse-temurin-21-alpine'
        }
    }

    parameters {
        choice(name: 'INCLUDED_GROUPS', choices: ['smoke', 'regression', 'slow', 'all'], description: 'Select test group to include in test run')
        choice(name: 'EXCLUDED_GROUPS', choices: ['smoke', 'regression', 'slow', 'all', 'none'], description: 'Select test group to exclude in test run')
        booleanParam(name: 'RUN_SONAR_QUBE', defaultValue: false, description: 'Run SonarQube analysis')
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build')
        string(name: 'ARGS', defaultValue: '-Drp.launch=identity_tests -Drp.attributes="group:test_infra;test_type:backend;team:identity"', description: "Report portal params")
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: params.BRANCH_NAME, url: 'https://github.com/automationhacks/test-infra.git'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Test') {
            steps {
                script {
                    sh './gradlew test -DincludedGroups=${INCLUDED_GROUPS} ${ARGS} --info'
                }
            }
        }

        stage('SonarQube Analysis') {
            when {
                expression { params.RUN_SONAR_QUBE }
            }

            steps {
                withSonarQubeEnv('SonarQube') {
                    sh './gradlew sonarqube'
                }
            }
        }

        stage('Generate Reports') {
            steps {
                junit '**/build/test-results/test/*.xml'
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}