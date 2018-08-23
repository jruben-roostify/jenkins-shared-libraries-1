# jenkins-shared-libraries

## Common Jenkins library for Java projects to be used accross the organization.

Usage

``` 
@Library('jenkins-shared-libraries') _  
def config = [runTest:true,buildFilePath:"./build.gradle",sonarNeeded:true]    
gradlePipeline(config)
```
