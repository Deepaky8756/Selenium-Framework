pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    parameters {
        choice(
            name: 'TEST_SUITE',
            choices: ['Smoke', 'Regression', 'All'],
            description: 'Select the test suite to execute'
        )

        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Select the browser'
        )

        choice(
            name: 'HEADLESS',
            choices: ['true', 'false'],
            description: 'Run browser in headless mode (recommended for Jenkins agents)'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn -B clean compile -DskipTests'
                    } else {
                        bat 'mvn -B clean compile -DskipTests'
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def runner = "TestRunner${params.TEST_SUITE}"
                    def command = "mvn -B clean test -Dtest=${runner} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS}"

                    if (isUnix()) {
                        sh command
                    } else {
                        bat command
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts(
                artifacts: 'target/**/*.html,target/**/*.json,target/**/*.xlsx,test-output/**/*',
                allowEmptyArchive: true
            )

            junit(
                testResults: 'target/surefire-reports/*.xml',
                allowEmptyResults: true
            )
        }

        success {
            echo 'Automation tests completed successfully.'
        }

        failure {
            echo 'Automation tests failed. Check the console and archived reports.'
        }
    }
}
