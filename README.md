# Github Parser
### A simple parser for github repositories

### Clone project
```bash
git clone git@github.com:lirugo/github-parser.git
```

### How to run with docker-compose

```bash
# it is an optional step but might cause an api request limit
sed -i 's/YOUR_GITHUB_TOKEN/ghp_xxxxxxxxxxxxxxxx/g' ./local/docker-compose.yaml
# run 
docker-compose -f ./local/docker-compose.yaml up -d
```

### How to run with docker
```bash
# it is an optional step but might cause an api request limit
# -e GITHUB_TOKEN=ghp_xxxxxxxxxxxxxxxx 
# build
docker build . -t github-parser
# run
docker run -p 8585:8585 -e GITHUB_TOKEN=ghp_xxxxxxxxxxxxxxxx github-parser:latest
```

### How to use
```bash
### Get all repos for user or organization
# OWNER - github user or organization name, required parameter
curl -X GET http://localhost:8585/api/v1/github-parser/repos?owner=${OWNER}

### Get all files for user or organization
# OWNER - github user or organization name, required parameter
# REGEX - regular expression for file name ex: .*.js, optional parameter default value is README.md
# FILE_LIMIT - limit of files to return, optional parameter default value is 1000
curl -X GET http://localhost:8585/api/v1/github-parser/files?owner=${OWNER}&fileRegExp=${REGEX}&fileLimit=${FILE_LIMIT}

### Get word frequency from files for user or organization
# OWNER - github user or organization name, required parameter
# REGEX - regular expression for file name ex: .*.js, README.md, optional parameter default value is README.md
# FILE_LIMIT - limit of files to return, optional parameter default value is 1000
# MIN_LETTER - limit of letters to return, optional parameter default value is 3
curl -X GET http://localhost:8585/api/v1/github-parser/word-frequency?owner=${OWNER}&fileRegExp=${REGEX}&fileLimit=${FILE_LIMIT}&minLetter=${MIN_LETTER}
```

