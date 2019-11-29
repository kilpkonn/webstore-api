INSERT INTO webstore.category(id, name) VALUES
    (100, 'green'),
    (101, 'blue'),
    (102, 'red');

INSERT INTO webstore.product(amount, description, name, price, category, image_url) VALUES
    (5, 'Agapanthus is known as the Flower of Love, the name comes from the Greek word agape, meaning love & anthos, meaning flower. It has spectacular funnel-shaped florets in a round cluster that is 10 to 15 cm (4 to 6") across. This is a true focal flower.',
     'Agapanthus', 20.55, 101, 'agapanthus.jpg'),
    (10, 'Bells of Ireland, or Shell Flower, is a half-hardy annual that produces an unusual pale green to emerald green funnel-shaped “bells” along green stems in the summer. Despite the common name, this plant from the mint family (Lamiaceae) is not from Ireland.',
     'Bells of Ireland', 9.99, 100, 'bells_of_ireland.jpg'),
    (10, 'Symbolizes pride and beauty. The earliest Carnations bore flesh-coloured flowers which gave rise to the name. Carnations are flowers appreciated for their ruffled appearance and clove-like scent.',
     'Carnation', 9.11, 102, 'carnation.jpg'),
    (87, 'A species of flower which includes daisy pompons, cushion pompons, spider pompons, mums and more. Learn about the different varieties.',
     'Chrysanthemum', 10.50, 102, 'chrysanthemum.jpg'),
    (293, 'Symbolizes regard and chivalry and is indicative of rebirth, new beginnings and eternal life.',
     'Daffodil', 3.50, null, 'daffodil.jpg'),
    (101, 'Ornithogalum (or-ni-THOG-a-lum) is a bulb flower native to Europe, Africa and western Asia. Although Star of Bethlehem is the common name of this genus, various species have different common names that are specific to them.',
     'Ornithogalum', 6.50, null, 'ornithogalum.jpg'),
    (0, 'Violets are a flexible flower! Widely cultivated for thousands of years they are used for eating and medicine; as well as a beautiful house plant for decoration! Their scientific name is Viola spp from the family Violaceae.',
     'Violet', 2.22, null, 'violet.jpg');

INSERT INTO webstore.news(content, created_at, headline) VALUES
    ('We are happy to announce that we are now taking orders. For information on how to reach us please visit the contact page.', '2019-06-22 19:10:25', 'Welcome to the flower store!'),
    ('We have added several new flowers to our store, among them are Chrysanthemums and Daffodils.', '2019-07-10 19:10:25', 'New flowers in stock!'),
    ('We are changing our hosting provider so the store will be unavailable from Aug 11 to Aug 13. Sorry for the inconvenience!', '2019-08-09 19:10:25', 'Imminent downtime.')

