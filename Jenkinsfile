pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    options {
        disableConcurrentBuilds()
        timeout(time: 60, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timestamps()
    }

    parameters {
        choice(name: 'TEST_SUITE', choices: ['Smoke', 'Regression', 'All'], description: 'Select the test suite to execute')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Select one browser for this build')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {
        stage('Clean Workspace') {
            steps { deleteDir() }
        }

        stage('Checkout') {
            steps {
                checkout scm
                echo "Branch: ${env.GIT_BRANCH ?: 'N/A'}"
                echo "Commit: ${env.GIT_COMMIT ?: 'N/A'}"
            }
        }

        stage('Verify Environment') {
            steps {
                echo "Suite=${params.TEST_SUITE}, Browser=${params.BROWSER}, Headless=${params.HEADLESS}"
                script {
                    if (isUnix()) {
                        sh '''
                            java -version
                            mvn -version
                            case "$BROWSER" in
                                chrome) google-chrome --version || chromium --version || true ;;
                                firefox) firefox --version || true ;;
                                edge) microsoft-edge --version || microsoft-edge-stable --version || true ;;
                            esac
                        '''
                    } else {
                        bat '''
                            java -version
                            mvn -version
                            if "%BROWSER%"=="chrome" (where chrome & chrome --version)
                            if "%BROWSER%"=="firefox" (where firefox & firefox --version)
                            if "%BROWSER%"=="edge" (where msedge & msedge --version)
                        '''
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) { sh 'mvn clean compile -DskipTests' }
                    else { bat 'mvn clean compile -DskipTests' }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def runnerClass = "TestRunner${params.TEST_SUITE}"
                    echo "Running ${runnerClass} on ${params.BROWSER} (headless=${params.HEADLESS})"
                    if (isUnix()) {
                        sh "mvn clean test -Dtest=${runnerClass} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS}"
                    } else {
                        bat "mvn clean test -Dtest=${runnerClass} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS}"
                    }
                }
            }
        }

        stage('Verify Reports') {
            steps {
                script {
                    if (isUnix()) { sh 'ls -la target || true; ls -la test-output || true' }
                    else { bat 'if exist target dir target & if exist test-output dir test-output' }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts(
                artifacts: 'target/**/*.html,target/**/*.json,target/**/*.xml,target/screenshots/**/*,target/test-results/**/*,test-output/**/*',
                allowEmptyArchive: true,
                fingerprint: true
            )
            junit(
                testResults: 'target/**/*.xml',
                allowEmptyResults: true,
                skipPublishingChecks: true
            )
        }
        success { echo "Automation tests passed: ${params.TEST_SUITE} / ${params.BROWSER}" }
        unstable { echo 'Automation tests are unstable. Check reports and screenshots.' }
        failure { echo 'Automation tests failed. Check Console Output, reports, and screenshots.' }
        aborted { echo 'Build aborted.' }
        cleanup { echo 'Jenkins pipeline execution completed.' }
    }
}
