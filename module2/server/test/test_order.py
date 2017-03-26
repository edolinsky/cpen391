from __future__ import absolute_import
from flask_testing import LiveServerTestCase
import requests
import server


class OrderTests(LiveServerTestCase):
    order_endpoint = "/order"

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
        self.params = {'table_id': '0xDEFEC7EDDA7ABA5E',
                       'restaurant_id': 'test_resto',
                       'customer_id': 'test_user',
                       'order_id': '26a00ff96d'}
        self.request_body = {'table_id': '0xDEFEC7EDDA7ABA5E',
                             'restaurant_id': 'test_resto',
                             'customer_id': 'test_user',
                             'items': [
                                 {
                                     "customer_name": "Erik",
                                     "id": "3838e0e86c",
                                     "menu_id": "2rs7U6patW",
                                     "status": "placed"
                                 }
                             ]
                             }

    def order_get(self, headers=None):
        return requests.get(self.get_server_url() + self.order_endpoint,
                            params=self.params, headers=headers)

    def order_get_csv(self):
        headers = {'Content-Type': 'text/csv'}
        return self.order_get(headers=headers)

    def order_post(self):
        return requests.post(self.get_server_url() + self.order_endpoint,
                             json=self.request_body)

    def test_get_success(self):
        r = self.order_get()

        self.assertEqual(r.status_code, server.OK)
        self.assertTrue('items' in r.json())

    def test_get_csv_success(self):
        r = self.order_get_csv()

        first_line = r.text.split('\n', 1)[0]
        self.assertEqual(r.status_code, server.OK)

        # Response should have 3 entries per line, so 2 commas.
        self.assertTrue(unicode.count(first_line, ',') == 2)

    def test_get_no_order_id(self):
        self.params.pop('order_id')

        r = self.order_get()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_get_no_restaurant(self):
        self.params.pop('restaurant_id')

        r = self.order_get()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_get_no_customer(self):
        self.params.pop('customer_id')

        r = self.order_get()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_get_no_table(self):
        self.params.pop('table_id')

        r = self.order_get()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_no_restaurant(self):
        self.request_body.pop('restaurant_id')

        r = self.order_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_no_customer(self):
        self.request_body.pop('customer_id')

        r = self.order_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_no_table(self):
        self.request_body.pop('table_id')

        r = self.order_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_restaurant_nonexistent(self):
        self.request_body.update({'restaurant_id': 'garbage'})

        r = self.order_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_customer_nonexistent(self):
        self.request_body.update({'customer_id': 'garbage'})

        r = self.order_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_table_nonexistent(self):
        self.request_body.update({'table_id': 'garbage'})

        r = self.order_post()

        self.assertEqual(r.status_code, server.UNAUTHORIZED)
        self.assertTrue('error' in r.json())

    def test_post_no_items(self):
        self.request_body.pop('items')

        r = self.order_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_empty_items(self):
        self.request_body.update({'items': []})

        r = self.order_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())
