import MySQLdb
from database import Database


class MenuDb(Database):
    item_types = ['drink', 'alcoholic', 'appetizer', 'entree', 'dessert', 'merchandise']

    def __init__(self, user, passwd, host, db, port='3306'):
        Database.__init__(self,
                          user=user,
                          passwd=passwd,
                          host=host,
                          db=db,
                          port=port)

    def connect(self):
        Database.connect(self)

    def close(self):
        Database.connect(self)

    def get_menu(self, restaurant_id):
        """
        Retrieve all items in a restaurant's menu given the restaurant's ID.
        :param restaurant_id: unique ID of a restaurant.
        :return: Dictionary containing menu items, or an error message.
        """
        self.connect()

        menu_info = {'restaurant_id': restaurant_id}

        query = ("SELECT id, description, name, price, type "
                 "FROM menu WHERE restaurant_id = '{}';").format(restaurant_id)
        try:
            self.cursor.execute(query)
            menu_items = self.cursor.fetchall()
            menu_info.update({'items': menu_items})

        except MySQLdb.Error:
            print "Error: Unable to fetch data."
            menu_info.update({'error': 'Unable to fetch menu data.'})

        self.close()

        return menu_info

    def get_sub_menu(self, restaurant_id, item_type):
        """
        Retrieve all items in a restaurant's menu of the specified item type.
        :param restaurant_id: unique ID of a restaurant.
        :param item_type: Food item type, enum of one of the following:
        :return:
        """
        self.connect()

        menu_info = {'restaurant_id': restaurant_id}

        query = ("SELECT id, description, name, price, type FROM menu "
                 "WHERE restaurant_id = '{}' AND type = '{}';").format(restaurant_id, item_type)
        try:
            self.cursor.execute(query)
            menu_items = self.cursor.fetchall()
            menu_info.update({'items': menu_items})

        except MySQLdb.Error:
            print "Error: Unable to fetch data."
            menu_info.update({'error': 'Unable to fetch menu data.'})

        self.close()

        return menu_info