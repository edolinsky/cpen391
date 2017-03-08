import flask
import flask_login
import os

from user import User

app = flask.Flask(__name__)
app.secret_key = os.environ['secret_key']

login_manager = flask_login.LoginManager()
login_manager.init_app(app)

db_user = os.environ['db_user']
db_passwd = os.environ['db_passwd']
db_host = os.environ['db_host']
db_port = os.environ['db_port']
db_database = os.environ['db']


@app.route('/', methods=['GET', 'POST'])
def login():
    if flask.request.method == 'GET':
        return '''
               <form action='login' method='POST'>
                <input type='text' name='email' id='email' placeholder='email'></input>
                <input type='password' name='pw' id='pw' placeholder='password'></input>
                <input type='submit' name='submit'></input>
               </form>
               '''

    user = User(flask.request.form['email'])

    if flask.request.form['pw'] == user.is_valid(flask.request.form['pw']):
        flask_login.login_user(user)
        return flask.redirect(flask.url_for('protected'))


def logout():
    flask_login.logout_user()
    return 'logged out'


@app.route('/hello')
def hello_world():
    return 'Hello World!'


if __name__ == '__main__':
    app.run()
