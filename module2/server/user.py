import flask_login
import server
from database import Database


class User(flask_login.UserMixin):

    def __init__(self, email):
        self.id = email

    def is_valid(self, passwd):
        db = Database(user=server.db_user,
                      passwd=server.db_passwd,
                      host=server.db_host,
                      db=server.db_database,
                      port=server.db_port)

        if passwd == db.get_user_password_by_email(self.id):
            return True
        else:
            return False

    def user_exists(self):
        db = Database(user=server.db_user,
                      passwd=server.db_passwd,
                      host=server.db_host,
                      db=server.db_database,
                      port=server.db_port)
        return db.user_exists(self.get_id())


@server.login_manager.user_loader
def user_loader(email):
    if email not in users:
        return

    user = User(email)
    return user


@server.login_manager.request_loader
def request_loader(request):
    email = request.form.get('email')
    if email not in users:
        return

    user = User()
    user.id = email

    # DO NOT ever store passwords in plaintext and always compare password
    # hashes using constant-time comparison!
    user.is_authenticated = request.form['pw'] == users[email]['pw']

    return user
