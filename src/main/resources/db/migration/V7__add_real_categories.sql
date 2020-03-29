DELETE FROM webstore.category c
WHERE c.id IN (100, 101, 102);

INSERT INTO webstore.category(id, name) VALUES
    (200, 'Tomatid'),
    (201, 'Kurgid'),
    (202, 'Tomatitaimed'),
    (203, 'Kurgitaimed'),
    (204, 'Võõrasemad'),
    (205, 'Begooniad'),
    (206, 'Pelargonid'),
    (207, 'Suvelilled'),
    (208, 'Püsililled'),
    (209, 'Hostad'),
    (210, 'Maitsetaimed'),
    (211, 'Toalilled'),
    (212, 'Amplid'),
    (213, 'Semperad'),
    (214, 'Lobeeliad'),
    (215, 'Hortensiad'),
    (216, 'Köögiviljataimed'),
    (217, 'Maasikataimed'),
    (218, 'Lõvilõuad');
