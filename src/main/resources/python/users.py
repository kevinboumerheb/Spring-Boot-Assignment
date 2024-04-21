import requests
import pymysql

# Fetch data from the URL
response = requests.get('https://jsonplaceholder.typicode.com/users')
data = response.json()

# Establish a connection to your MySQL database
connection = pymysql.connect(host='host',
                             user='user',
                             password='password',
                             db='db')

try:
    with connection.cursor() as cursor:
        # Iterate over the data
        for item in data:
            # Prepare the SQL INSERT statement
            sql = """
            INSERT IGNORE INTO `User` (`id`, `name`, `username`, `email`, `phone`, `website`, `street`, `suite`, `city`, `zipcode`, `lat`, `lng`, `company_name`, `catch_Phrase`, `bs`) 
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """
            # Execute the SQL statement
            cursor.execute(sql, (
                item['id'], 
                item['name'], 
                item['username'], 
                item['email'], 
                item['phone'], 
                item['website'], 
                item['address']['street'], 
                item['address']['suite'], 
                item['address']['city'], 
                item['address']['zipcode'], 
                item['address']['geo']['lat'], 
                item['address']['geo']['lng'], 
                item['company']['name'],
                item['company']['catchPhrase'], 
                item['company']['bs']
            ))

    # Commit the transaction
    connection.commit()
finally:
    # Close the connection
    connection.close()