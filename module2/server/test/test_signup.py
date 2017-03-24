from __future__ import absolute_import
from flask_testing import LiveServerTestCase
import requests
import server
from user import User


class SignupTests(LiveServerTestCase):
    signup_endpoint = "/signup"

    def create_app(self):
        app = server.app
        app.config['TESTING'] = True

        # Set to 0 to have the OS pick the port.
        app.config['LIVESERVER_PORT'] = 0
        return app

    @classmethod
    def setUpClass(cls):
        cls.user = User(email="garbage@garba.ge")

    @classmethod
    def tearDownClass(cls):
        pass

    def setUp(self):
        email = "garbage" + self.get_server_url() + "@garba.ge"
        self.user.email = email
        self.request_body = {"user": email,
                             "password": "password"}

    def tearDown(self):
        self.user.delete()

    def signup_post(self):
        return requests.post(url=self.get_server_url() + self.signup_endpoint,
                             json=self.request_body)

    def test_signup_customer(self):
        affinity = "customer"
        email = "garbage" + affinity + self.get_server_url() + "@garba.ge"
        self.user.email = email
        self.request_body.update({"user": email,
                                  "affinity": affinity})
        r = self.signup_post()

        self.assertTrue(self.user.exists())
        self.assertEqual(affinity, self.user.get_affinity())
        self.assertEqual(affinity, r.json().get('affinity', ''))

    def test_signup_staff(self):
        affinity = "staff"
        restaurant_id = "test_resto"
        email = "garbage" + affinity + self.get_server_url() + "@garba.ge"
        self.user.email = email
        self.request_body.update({"user": email,
                                  "affinity": affinity,
                                  "restaurant_id": restaurant_id})
        r = self.signup_post()
        user_id = r.json().get('id', '')

        self.assertTrue(self.user.exists())
        self.assertEqual(affinity, self.user.get_affinity())
        self.assertEqual(affinity, r.json().get('affinity', ''))
        self.assertEqual(restaurant_id, self.user.get_my_restaurant(user_id=user_id))

    def test_signup_staff_only(self):
        affinity = "staff_only"
        restaurant_id = "test_resto"
        email = "garbage" + affinity + self.get_server_url() + "@garba.ge"
        self.user.email = email
        self.request_body.update({"user": email,
                                  "affinity": affinity,
                                  "restaurant_id": restaurant_id})
        r = self.signup_post()
        user_id = r.json().get('id', '')

        self.assertTrue(self.user.exists())
        self.assertEqual(affinity, self.user.get_affinity())
        self.assertEqual(affinity, r.json().get('affinity', ''))
        self.assertEqual(restaurant_id, self.user.get_my_restaurant(user_id=user_id))

    def test_signup_exists(self):
        # Sign up twice, so that we can get the error the second time.
        r1 = self.signup_post()

        self.assertEqual(r1.status_code, server.CREATED)

        r2 = self.signup_post()

        self.assertEqual(r2.status_code, server.FORBIDDEN)
        self.assertTrue('error' in r2.json())

    def test_signup_android_reg(self):
        reg_one = "Return of the Jedi"
        self.request_body.update({"android_reg_id": reg_one})
        r = self.signup_post()

        self.assertEqual(r.status_code, server.CREATED)
        user_id = r.json().get('id', '')
        self.assertEqual(self.user.get_app_id(user_id=user_id), reg_one)

    def test_signup_no_user(self):
        self.request_body.pop("user")
        r = self.signup_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertFalse(self.user.exists())

    def test_signup_no_password(self):
        self.request_body.pop("password")
        r = self.signup_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertFalse(self.user.exists())
