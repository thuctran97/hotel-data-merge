var db = db.getSiblingDB('hotel-data-merge');

db.suppliers.insert(
    [
        {
            "name": "acme",
            "url": "https://5f2be0b4ffc88500167b85a0.mockapi.io/suppliers/acme",
            "dataPriorityLevel": 1,
            "mappingSchema": {
                "id": "Id",
                "name": "Name",
                "location.destinationId": "DestinationId",
                "location.country": "Country",
                "location.city": "City",
                "location.address": "Address",
                "location.latitude": "Latitude",
                "location.longitude": "Longitude",
                "description": "Description",
                "facilities": "Facilities"
            }
        },
        {
            "name": "patagonia",
            "url": "https://5f2be0b4ffc88500167b85a0.mockapi.io/suppliers/patagonia",
            "dataPriorityLevel": 2,
            "mappingSchema": {
                "id": "id",
                "name": "name",
                "location.destinationId": "destination",
                "location.address": "address",
                "location.latitude": "lat",
                "location.longitude": "lng",
                "description": "info",
                "amenities.name": "amenities.[]",
                "amenities.type": "general",
                "images.url": "images.*.url",
                "images.description": "images.*.description",
                "images.type": "images.*"
            }
        },
        {
            "name": "paperflies",
            "url": "https://5f2be0b4ffc88500167b85a0.mockapi.io/suppliers/paperflies",
            "dataPriorityLevel": 3,
            "mappingSchema": {
                "id": "hotel_id",
                "name": "hotel_name",
                "location.destinationId": "destination_id",
                "location.address": "location.address",
                "location.country": "location.country",
                "description": "details",
                "amenities.name": "amenities.*.[]",
                "amenities.type": "amenities.*",
                "images.url": "images.*.link",
                "images.description": "images.*.caption",
                "images.type": "images"
            }
        }
    ]
);