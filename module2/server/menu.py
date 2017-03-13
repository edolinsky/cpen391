import db
from db.menu_db import MenuDb


class Menu:
    def __init__(self, restaurant_id):
        self.restaurant_id = restaurant_id
        self.db = MenuDb(user=db.db_user,
                         passwd=db.db_passwd,
                         host=db.db_host,
                         db=db.db_database,
                         port=db.db_port)

    def get_menu(self):
        pass

    def get_submenu(self, item_type):
        pass
