var db = db.getSiblingDB('hotel-data-merge');

db.suppliers.insert(
    [
        {
            "name": "acme",
            "url": "https://5f2be0b4ffc88500167b85a0.mockapi.io/suppliers/acme",
            "priorityLevel": 1,
            "referenceSchema": {
                "id": "Id",
                "name": "Name",
                "location": {
                    "destinationId": "DestinationId",
                    "country": "Country",
                    "city": "City",
                    "address": "Address",
                    "latitude": "Latitude",
                    "longitude": "Longitude",
                },
                "description": "Description",
                "facilities": "Facilities"
            }
        },
        {
            "name": "patagonia",
            "url": "https://5f2be0b4ffc88500167b85a0.mockapi.io/suppliers/patagonia",
            "priorityLevel": 2,
            "referenceSchema": {
                "id": "id",
                "name": "name",
                "location": {
                    "destinationId": "destination",
                    "address": "address",
                    "latitude": "lat",
                    "longitude": "lng",
                },
                "description": "info",
                "amenities":{
                    "name": "amenities",
                    "type": "general"
                },
                "images": {
                    "url": "images.*.url",
                    "description": "images.*.description",
                    "type": "images.*"
                }
            }
        },
        {
            "name": "paperflies",
            "url": "https://5f2be0b4ffc88500167b85a0.mockapi.io/suppliers/paperflies",
            "priorityLevel": 3,
            "referenceSchema": {
                "id": "hotel_id",
                "name": "hotel_name",
                "location":{
                    "destinationId": "destination_id",
                    "address": "location.address",
                    "country": "location.country",
                },

                "description": "details",
                "amenities":{
                    "name": "amenities.*.[]",
                    "type": "amenities.*"
                },
                "images": {
                    "url": "images.*.link",
                    "description": "images.*.caption",
                    "type": "images.*"
                },
                "bookingConditions":"booking_conditions"
            }
        }
    ]
);