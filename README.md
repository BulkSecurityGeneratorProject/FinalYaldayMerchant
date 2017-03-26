[![Build Status](https://travis-ci.org/Yalday/FinalYaldayMerchant.svg?branch=master)](https://travis-ci.org/Yalday/FinalYaldayMerchant)

# FinalYaldayMerchant

This project is the main user service for Yalday

## Development

To start the application in the dev profile, simply run:

    ./mvnw
   
## Staging

To start the application in the staging
 
    export JHIPSTER_REGISTRY_USER=admin
    export JHIPSTER_REGISTRY_PASSWORD=admin
    export JHIPSTER_REGISTRY_HOST=yalday-jhipsterregistry.herokuapp.com
    export PORT=8080
    
    ./mvnw -Pdev,heroku clean package
      
      OR
      
    mvn clean package; java -jar target/*.war --spring.profiles.active=dev,heroku --server.port=8080
    
If ran locally, this will connect to the staging jhipster registry.    

## Building for production

We are not there yet...


## Testing

To launch your application's tests, run:

    mvn clean test
    
### Other tests

Performance tests are run by [Gatling][] and written in Scala. They're located in `src/test/gatling` and can be run with:

    ./mvnw gatling:execute

For more information, refer to the [Running tests page][].


## Contributing

### To submit a change do the following

     git pull (--rebase)
     git checkout -b "some_sensible_branch_name"
     <make your changes>
     git commit -am "some sensible commit message"
     git push -u origin some_sensible_branch_name
     
### Conflicts

If you need to make changes to an existing branch, you should always make sure it has the latest master
already included. This is done by doing 

     git checkout master
     git pull
     git checkout "some_sensible_branch_name" 
     git rebase master

Rebasing has some major advantages to merging from master to the branch. Simply put, you don't end up with an extra 
commit and the history tree is kept sane. Here is a nice article https://www.atlassian.com/git/tutorials/merging-vs-rebasing
      
    
When you rebase your branch onto master you may have conflicts. So at this point you will be in the middle of a rebase.
This gives you the chance to fix them by selecting the correct code to use (I use intellij for this but you can use whatever feels most confortable).
Once all conflicts are resolved run:

    git rebase --continue
    
then 
   
    git push -f
    
IMPORTANT: This is a force push and will OVERWRITE whatever is in the remote repository. NEVER run this command
           on master
    
## Continuous integration

All branches and pull requests are ran on travis. The current master status is displayed at the top
of this file, click this to go to the travis build, the details of which can be seen .travis.yml.
     


