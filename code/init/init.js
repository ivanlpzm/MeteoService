db = db.getSiblingDB('meteoservice');

db.temperatureData.createIndex(
  { latitude: 1, longitude: 1 },
  { unique: true }
);

db.temperatureData.createIndex(
  { timestamp: 1 },
  { expireAfterSeconds: 60 } 
);
