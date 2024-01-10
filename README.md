# Github Parser
### A simple parser for github repositories

## Insert github token

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
export GITHUB_TOKEN=your_token 
# build
docker build . -t github-parser
# run
docker run -p 8585:8585 github-parser:latest
```

