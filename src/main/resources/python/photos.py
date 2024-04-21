import requests
import pymysql

# Fetch data from the URL
response = requests.get('https://jsonplaceholder.typicode.com/photos')
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
            INSERT IGNORE INTO `Photo` (`id`, `album_Id`, `title`, `url`, `thumbnail_Url`) 
            VALUES (%s, %s, %s, %s, %s)
            """
            # Execute the SQL statement
            cursor.execute(sql, (
                item['id'], 
                item['albumId'], 
                item['title'], 
                item['url'], 
                item['thumbnailUrl']
            ))

    # Commit the transaction
    connection.commit()
finally:
    # Close the connection
    connection.close()