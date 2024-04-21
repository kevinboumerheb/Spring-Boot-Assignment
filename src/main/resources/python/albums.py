import requests
import pymysql

# Fetch data from the URL
response = requests.get('https://jsonplaceholder.typicode.com/albums')
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
            sql = "INSERT IGNORE INTO `Album` (`id`, `user_Id`, `title`) VALUES (%s, %s, %s)"
            # Execute the SQL statement
            cursor.execute(sql, (item['id'], item['userId'], item['title']))

    # Commit the transaction
    connection.commit()
finally:
    # Close the connection
    connection.close()