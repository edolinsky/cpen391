from __future__ import absolute_import
from flask_testing import LiveServerTestCase
import requests
import server
from menu import Menu


class MenuTests(LiveServerTestCase):
    menu_endpoint = "/menu"

    def create_app(self):
        app = server.app
        app.config['TESTING'] = True

        # Set to 0 to have the OS pick the port.
        app.config['LIVESERVER_PORT'] = 0
        return app

    @classmethod
    def setUpClass(cls):
        pass

    @classmethod
    def tearDownClass(cls):
        pass

    def setUp(self):
        restaurant_id = 'test_resto'
        self.menu = Menu(restaurant_id=restaurant_id)
        self.request_body = {'restaurant_id': restaurant_id}

    def menu_get(self, item_type=''):
        params = self.request_body
        if item_type:
            params.update({'item_type': item_type})

        return requests.get(self.get_server_url() + self.menu_endpoint,
                            params=params)

    def menu_post(self):
        return requests.post(self.get_server_url() + self.menu_endpoint,
                             json=self.request_body)

    def test_get_no_restaurant(self):
        self.request_body.pop('restaurant_id')

        r = self.menu_get()
        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_get_invalid_restaurant(self):
        self.request_body.update({'restaurant_id': self.get_server_url()})

        r = self.menu_get()
        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_get_success(self):
        r = self.menu_get()

        self.assertEqual(r.status_code, server.OK)
        self.assertTrue('items' in r.json())

    def test_get_types(self):
        for item_type in self.menu.db.item_types:
            r = self.menu_get(item_type=item_type)

            self.assertEqual(r.status_code, server.OK)
            items = r.json().get('items', [])
            for item in items:
                self.assertEqual(item.get('type', ''), item_type)

    def test_get_invalid_item(self):
        r = self.menu_get(item_type='garbage')

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_no_restaurant(self):
        self.request_body.pop('restaurant_id')

        r = self.menu_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_invalid_restaurant(self):
        self.request_body.update({'restaurant_id': self.get_server_url(),
                                  'items': [{'name': 'apples',
                                             'type': 'dessert',
                                             'description': 'Whistle while you work.',
                                             'price': 42.00
                                             }
                                            ]
                                  }
                                 )
        r = self.menu_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_no_items(self):
        r = self.menu_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_empty_items(self):
        self.request_body.update({'restaurant_id': 'test_resto',
                                  'items': []})
        r = self.menu_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())
