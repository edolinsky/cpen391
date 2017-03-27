import db
import copy
from db.menu_db import MenuDb


class Menu:
    def __init__(self, restaurant_id):
        self.restaurant_id = restaurant_id
        self.db = MenuDb(user=db.db_user,
                         passwd=db.db_passwd,
                         host=db.db_host,
                         db=db.db_database,
                         port=db.db_port)

    def create_menu(self, menu_info):
        """
        Creates menu item entries in the database.
        :param menu_info: Dictionary containing list of item objects 'items'.
        :return: JSON response body.
        """
        updated_items = []
        for item in menu_info['items']:
            item_copy = copy.copy(item)
            item_copy['id'] = self.db.generate_id()
            updated_items.append(item_copy)

        updated_menu = {'items': updated_items, 'restaurant_id': self.restaurant_id}

        return self.db.create_menu(menu_info=updated_menu)

    def get_menu(self):
        """
        Retrieve the menu given this menu's restaurant ID.
        :return:
        """
        return self.db.get_menu(restaurant_id=self.restaurant_id)

    def get_submenu(self, item_type):
        """
        Retrieve a portion of this restaurant's menu given a particular item type.
        :param item_type:
        :return:
        """
        return self.db.get_sub_menu(restaurant_id=self.restaurant_id,
                                    item_type=item_type)
