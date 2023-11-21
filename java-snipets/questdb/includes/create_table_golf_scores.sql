CREATE TABLE golf_scores
AS(
    SELECT
        x ID,
        rnd_timestamp( to_timestamp('2015', 'yyyy'), to_timestamp('2016', 'yyyy'), 0)ts
    FROM long_sequence(1000) x)
TIMESTAMP(ts);