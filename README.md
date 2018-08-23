# jenkins-shared-libraries

## Common Jenkins library for Java projects to be used accross the organization.

Pre-requsite
```
Make sure the following variables are configured on Jenkins
1. SONAR_URL -- Sonarqube url to use.
2. SONAR_PASSWORD -- Sonarqube token.
3. GITHUB_OAUTH_TOKEN -- Github token

Install jacoco plugin on jenkins.
```

Usage

``` 
@Library('jenkins-shared-libraries') _  
def config = [runTest:true,buildFilePath:"./build.gradle",sonarNeeded:true]    
gradlePipeline(config)
```
