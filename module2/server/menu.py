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
        return self.db.get_menu(restaurant_id=self.restaurant_id)

    def get_submenu(self, item_type):
        return self.db.get_sub_menu(restaurant_id=self.restaurant_id,
                                    item_type=item_type)
