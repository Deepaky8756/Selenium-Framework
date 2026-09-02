pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    options {
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
        timeout(time: 60, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timestamps()
    }

    parameters {
        choice(name: 'TEST_SUITE', choices: ['Smoke', 'Regression', 'All'],
               description: 'Select the test suite to execute')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'],
               description: 'Select one browser for this build')
        booleanParam(name: 'HEADLESS', defaultValue: true,
                     description: 'Run browser in headless mode')
        choice(name: 'TEST_ENV', choices: ['QA', 'UAT'],
               description: 'Select the application environment')
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {

        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
                echo "Job: ${env.JOB_NAME}"
                echo "Build: ${env.BUILD_NUMBER}"
                echo "Branch: ${env.GIT_BRANCH ?: 'N/A'}"
                echo "Commit: ${env.GIT_COMMIT ?: 'N/A'}"
            }
        }

        stage('Verify Environment') {
            steps {
                echo "Suite=${params.TEST_SUITE}, Browser=${params.BROWSER}, Headless=${params.HEADLESS}, Environment=${params.TEST_ENV}"

                script {
                    if (isUnix()) {
                        sh '''
                            echo "===== JAVA ====="
                            java -version
                            echo "===== MAVEN ====="
                            mvn -version
                            echo "===== BROWSER ====="
                            case "$BROWSER" in
                                chrome) google-chrome --version || chromium --version || true ;;
                                firefox) firefox --version || true ;;
                                edge) microsoft-edge --version || microsoft-edge-stable --version || true ;;
                            esac
                        '''
                    } else {
                        bat '''
                            echo ===== JAVA =====
                            java -version
                            echo ===== MAVEN =====
                            mvn -version
                            echo ===== BROWSER =====
                            if "%BROWSER%"=="chrome" (
                                where chrome
                                chrome --version
                            )
                            if "%BROWSER%"=="firefox" (
                                where firefox
                                firefox --version
                            )
                            if "%BROWSER%"=="edge" (
                                where msedge
                                msedge --version
                            )
                        '''
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean compile -DskipTests'
                    } else {
                        bat 'mvn clean compile -DskipTests'
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def runnerClass = "TestRunner${params.TEST_SUITE}"

                    echo "Running ${runnerClass} on ${params.BROWSER}, headless=${params.HEADLESS}, environment=${params.TEST_ENV}"

                    if (isUnix()) {
                        sh "mvn clean test -Dtest=${runnerClass} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS} -DtestEnv=${params.TEST_ENV}"
                    } else {
                        bat "mvn clean test -Dtest=${runnerClass} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS} -DtestEnv=${params.TEST_ENV}"
                    }
                }
            }
        }

        stage('Verify Reports') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            echo "===== TARGET ====="
                            ls -la target || true
                            echo "===== TEST OUTPUT ====="
                            ls -la test-output || true
                            echo "===== REPORTS ====="
                            find target -type f \( -name "*.html" -o -name "*.json" -o -name "*.xml" \) || true
                        '''
                    } else {
                        bat '''
                            echo ===== TARGET =====
                            if exist target dir target
                            echo ===== TEST OUTPUT =====
                            if exist test-output dir test-output
                            echo ===== REPORTS =====
                            if exist target (
                                dir /s /b target\*.html
                                dir /s /b target\*.json
                                dir /s /b target\*.xml
                            )
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Collecting reports, screenshots and test artifacts..."

            archiveArtifacts(
                artifacts: 'target/**/*.html,target/**/*.json,target/**/*.xml,target/screenshots/**/*,target/test-results/**/*,test-output/**/*,**/*.log',
                allowEmptyArchive: true,
                fingerprint: true
            )

            junit(
                testResults: 'target/**/*.xml',
                allowEmptyResults: true,
                skipPublishingChecks: true
            )
        }

        success {
            echo "TEST EXECUTION SUCCESSFUL"
            echo "Suite=${params.TEST_SUITE}, Browser=${params.BROWSER}, Environment=${params.TEST_ENV}"
        }

        unstable {
            echo "TEST EXECUTION UNSTABLE - Check reports and screenshots."
        }

        failure {
            echo "TEST EXECUTION FAILED"
            echo "Suite=${params.TEST_SUITE}, Browser=${params.BROWSER}, Environment=${params.TEST_ENV}"
            echo "Check Console Output, Cucumber reports, screenshots and JUnit/TestNG results."
        }

        aborted {
            echo "Jenkins build was aborted."
        }

        cleanup {
            echo "Jenkins pipeline execution completed."
        }
    }
}
