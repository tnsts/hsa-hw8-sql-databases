# InnoDB Performance measurement

This repository provides an implementation of simple Spring Boot application that generates mock User entities
with specified params and saves them to MySQL with InnoDB engine to further its performance analysis.

This repository contains a Spring Boot application designed to generate mock User entities with specified parameters and
store them in a MySQL database using the InnoDB engine. The application facilitates performance analysis of database
operations.

## Quick Start

To start the application stack, run the following command:

```
docker compose up
```

Once the application is running, you can start seeding the database with mock data by sending the following HTTP
request:

```
POST http://localhost:8080/api/users/generate?amount=${AMOUNT}&birthdateFrom=${DOB_FROM}&birthdayTo=${DOB_TO}
```

### Request Parameters:

- **amount**: The number of users to generate.
- **birthdateFrom**: The earliest possible birthdate value for users (format: YYYY-MM-DD).
- **birthdateTo**: The latest possible birthdate value for users (format: YYYY-MM-DD).

## Experiments and results

To populate the database, the users table was created and seeded with 40M entries, each assigned a random birthdate
between 1990-01-01 and 2010-12-31:

```
POST http://localhost:8080/api/users/generate?amount=40000000&birthdateFrom=1990-01-01&birthdayTo=2010-12-31
```

### Indexes

The performance of SELECT queries on the birthdate column was analyzed under different conditions,
including varying selection volumes and the presence or absence of BTREE and HASH indexes:

|                  | 0.01%     | 0.05%       | 0.1%     | 0.5%        | 1%          | 5%           |
|------------------|-----------|-------------|----------|-------------|-------------|--------------|
| Without index    | 19.731719 | 19.94630325 | 21.01226 | 21.30405    | 21.61528125 | 23.82828975  |
| With BTREE index | 1.031064  | 4.4125405   | 8.355073 | 41.56672125 | 86.62262325 | 410.57938525 |
| With HASH index  | BTREE     | BTREE       | BTREE    | BTREE       | BTREE       | BTREE        |

The values for HASH index refer to BTREE index results, as InnoDB silently changes HASH indexes into BTREE.

### innodb_flush_log_at_trx_commit

The impact of the innodb_flush_log_at_trx_commit value was assessed by measuring insert speed while varying this setting
with different and different ops per second:

|     | 10 / sec | 100 / sec | 250 / sec |
|-----|----------|-----------|-----------|
| = 0 | 0.032    | 0.243     | 0.651     |
| = 1 | 0.038    | 0.257     | 0.686     |
| = 2 | 0.034    | 0.248     | 0.667     |