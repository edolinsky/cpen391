from __future__ import absolute_import
from flask_testing import LiveServerTestCase
import requests
import server


class OrdersTests(LiveServerTestCase):
    orders_endpoint = "/orders"

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
        self.params = {'restaurant_id': 'test_resto',
                       'query': 'open'}
        self.request_body = {"restaurant_id": "test_resto",
                             "items": [
                                 {
                                     "id": "3838e0e86c",
                                     "status": "placed",
                                     "order_id": "3be0d4a448"
                                 }
                             ]
                             }
        self.order_id = "3be0d4a448"

    def orders_get(self):
        return requests.get(self.get_server_url() + self.orders_endpoint,
                            params=self.params)

    def orders_patch(self):
        return requests.patch(self.get_server_url() + self.orders_endpoint,
                              json=self.request_body)

    def test_get_success(self):
        r = self.orders_get()

        self.assertEqual(r.status_code, server.OK)
        self.assertTrue('orders' in r.json())

    def test_get_not_supported(self):
        self.params.update({'query': 'garbage'})
        r = self.orders_get()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_get_no_restaurant(self):
        self.params.pop('restaurant_id')
        r = self.orders_get()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_get_restaurant_nonexistent(self):
        self.params.update({'restaurant_id': 'garbage'})
        r = self.orders_get()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_patch_success(self):
        r1 = self.orders_patch()

        self.assertEqual(r1.status_code, server.OK)
        self.assertEqual(r1.json()['items'][0]['status'], 'placed')

        self.request_body['items'][0].update({'status': 'prep'})
        r2 = self.orders_patch()

        self.assertEqual(r2.status_code, server.OK)
        self.assertEqual(r2.json()['items'][0]['status'], 'prep')

    def test_patch_no_restaurant(self):
        self.request_body.pop('restaurant_id')

        r = self.orders_patch()
        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_patch_restaurant_nonexistent(self):
        self.request_body.update({'restaurant_id': 'garbage'})

        r = self.orders_patch()
        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_patch_empty_items(self):
        self.request_body.update({'items': []})

        r = self.orders_patch()
        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_patch_no_items(self):
        self.request_body.pop('items')

        r = self.orders_patch()
        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_patch_no_id(self):
        self.request_body['items'][0].pop('id')

        r = self.orders_patch()
        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_patch_no_order_id(self):
        self.request_body['items'][0].pop('order_id')

        r = self.orders_patch()
        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_patch_no_status(self):
        self.request_body['items'][0].pop('status')

        r = self.orders_patch()
        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())
