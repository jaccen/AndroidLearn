.ignore不起效
需要执行以下代码
git rm -r --cached .

git add .

git commit -m 'update .gitignore'

git push origin master