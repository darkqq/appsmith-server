#!/bin/bash
sleep 10

echo SETUP.sh time now: `date +"%T" `
mongo --host mongo:27017 <<EOF
 var cfg = {
   "_id": "rs0",
   "version": 1,
   "members": [
     {
       "_id": 0,
       "host": "mongo:27017",
       "priority": 1
     }
   ]
 };
 rs.initiate(cfg, {force: true});
 rs.reconfig(cfg, {force: true});
EOF

