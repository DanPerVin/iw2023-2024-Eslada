INSERT INTO `appuser` (`id`, `dni`, `email`, `name`, `password`, `username`, `surname`) VALUES
(0x067384a2613f4b2fb530f3ad112e36c4, '11230123A', 'admin@example.com', 'admin', '$2a$10$8K0zBeb/kILiCc4VpQgF..hZE0xm2kWWrUyFF4Xhv7h9.wnmy19iu', 'admin', 'Rodriguez'),
(0x0d1f3992167c437ba4e1e8ce27b778c4, '83091828C', 'maria@example.com', 'Maria', '$2a$10$gAXBrsZdWyNMvl2XzcYdKeOrs1eqVLtGHeIHrFIFCGWxyYMNaYBcq', 'maria', 'Torres'),
(0x836aafe3aa694012a4b8a615cd62cde7, '21371283C', 'marketing@example.com', 'Marketing', '$2a$10$D8zTAVgJWc1JrMV/IdjVpe4OipntSOoXecTHyzqfzt84n.qSO3esm', 'marketing', 'Rodriguez'),
(0x847affdd9c764520aeb2472658dee3fd, '82138123C', 'facturas@example.com', 'Facturas', '$2a$10$CRa/xKDn8dAky5FxJD52GOYP6F2JkXzHnLi72zr7FLElOzJyrXFAG', 'facturas', 'Rodriguez'),
(0xa606832c6f3c47a38b8fecca0c5bd0ca, '19028380C', 'antonio@example.com', 'antonio', '$2a$10$JxDKBNXev9KuC8gPYTGkdO83ppjixEk8UVEngHTWVK.BoTm1wzkQS', 'antonio', 'Perez'),
(0xeab6e47b37cb47888169f86a0dcf81cb, '21387123C', 'atencion@example.com', 'Atencion', '$2a$10$lAKL0buUN3lx4e/BgV4Ao.Dm84bPk/f8PN548n65Z6TkZBuirSgvy', 'atencion', 'Rodriguez');

INSERT INTO `rol` (`id`, `name`) VALUES
(0x00000000000000000000000000000000, 'USER'),
(0x01000000000000000000000000000000, 'ADMIN'),
(0x03000000000000000000000000000000, 'FACTURAS'),
(0x04000000000000000000000000000000, 'MARKETING'),
(0x05000000000000000000000000000000, 'ATENCION');

INSERT INTO `consultation` (`id`, `closed`, `creation_date`, `name`, `user_id`) VALUES
(0xcb8c82e1b47d4031bbd9e3fcb9a346d4, b'0', '2024-01-11 01:44:19.000000', 'Contratar paquete Netflilx', 0xa606832c6f3c47a38b8fecca0c5bd0ca);

INSERT INTO `servicio_type` (`id`, `name`) VALUES
(0x01000000000000000000000000000000, 'Fibra'),
(0x02000000000000000000000000000000, 'Móvil'),
(0x7608d675ecef4f06925bd120f1164360, 'Television');

INSERT INTO `servicio` (`id`, `description`, `name`, `price`, `servicio_type_id`) VALUES
(0x1733bc4be2f440b3ae83e6cbf989cb81, 'Línea de teléfono móvil con capacidad 5G de datos.', 'Linea 5G', 25.5, 0x02000000000000000000000000000000),
(0x2d7abb08de374bf682e0ef264603c45a, 'Línea de teléfono móvil con capacidad 4G de datos.', 'Linea con 4G', 17.5, 0x02000000000000000000000000000000),
(0x3e53dc3167ab4f73bc42d047da87ae32, 'Internet en casa con  600MB de velocidad.', 'Fibra 600MB', 33.5, 0x01000000000000000000000000000000),
(0xd41560b08ab144a387acc0d4f376041a, 'Servicios de Television con 80 canales y Netflix incluido. No te pierdas Stranger Things.', 'Paquete Netflix', 60, 0x7608d675ecef4f06925bd120f1164360);

INSERT INTO `contrato` (`id`, `apellidos`, `direccion`, `dni`, `email`, `fecha`, `iban`, `nombre`, `user_id`) VALUES
(0xa292f3b98bb54fd180af08aa63b9d10e, 'Torres', 'Calle Venezuela 5C', '83091828C', 'maria@example.com', '2024-01-11 01:46:19.000000', 'ES8325322123433', 'Maria', 0x0d1f3992167c437ba4e1e8ce27b778c4),
(0xa6c60c00f0b9416aa7efb2e73e5ea78a, 'Perez', 'Calle Vicario 3A', '19028380C', 'antonio@example.com', '2024-01-11 01:43:32.000000', 'ES8423321123333', 'antonio', 0xa606832c6f3c47a38b8fecca0c5bd0ca);

INSERT INTO `contrato_servicio` (`contrato_id`, `servicio_id`) VALUES
(0xa6c60c00f0b9416aa7efb2e73e5ea78a, 0x1733bc4be2f440b3ae83e6cbf989cb81),
(0xa6c60c00f0b9416aa7efb2e73e5ea78a, 0x2d7abb08de374bf682e0ef264603c45a),
(0xa6c60c00f0b9416aa7efb2e73e5ea78a, 0x3e53dc3167ab4f73bc42d047da87ae32),
(0xa292f3b98bb54fd180af08aa63b9d10e, 0x1733bc4be2f440b3ae83e6cbf989cb81);

INSERT INTO `factura` (`id`, `fecha`, `contrato_id`, `user_id`) VALUES
(0x16eb0bee1bd14ce08d5dcd41409efd53, '2024-01-11 01:46:19.000000', 0xa292f3b98bb54fd180af08aa63b9d10e, 0x0d1f3992167c437ba4e1e8ce27b778c4),
(0x8329db1bcaa541edad6cc7059d64aa55, '2024-01-11 01:43:32.000000', 0xa6c60c00f0b9416aa7efb2e73e5ea78a, 0xa606832c6f3c47a38b8fecca0c5bd0ca);

INSERT INTO `linea` (`id`, `block`, `line`, `roaming`, `user_id`) VALUES
(0x02000000000000000000000000000000, 0, '2aeb96f0-130e-4128-b6a3-eed3bea1a7c3', 1, 0xa606832c6f3c47a38b8fecca0c5bd0ca),
(0x03000000000000000000000000000000, 0, '7de13dbc-8a72-4537-8392-339fe93f23b0', 0, 0xa606832c6f3c47a38b8fecca0c5bd0ca),
(0x4a844a8c51d24a24810f13437c56faac, 0, '8635559c-0fb3-466a-a354-41782158b59b', 0, 0x0d1f3992167c437ba4e1e8ce27b778c4);

INSERT INTO `message` (`id`, `creation_date`, `message_string`, `consultation_id`, `user_id`) VALUES
(0x2e4211deedf942288961a3eed7622800, '2024-01-11 01:53:57.000000', 'Además no tenemos paquete \"Netflilx\"', 0xcb8c82e1b47d4031bbd9e3fcb9a346d4, 0xeab6e47b37cb47888169f86a0dcf81cb),
(0x5447ea2f0a194867b67fabdf652fbd1a, '2024-01-11 01:44:47.000000', 'Quería enterarme mejor de cuanto cuesta el Paquete Netflix y si tengo alguna ventaja por todas las cosas que tengo ya contratadas !', 0xcb8c82e1b47d4031bbd9e3fcb9a346d4, 0xa606832c6f3c47a38b8fecca0c5bd0ca),
(0xa7839652780c47ada9e3f4bc09f08b37, '2024-01-11 01:47:44.000000', 'Antonio que no, deja de intentar conseguir descuentos por la cara, pesado.', 0xcb8c82e1b47d4031bbd9e3fcb9a346d4, 0x067384a2613f4b2fb530f3ad112e36c4);

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(0x067384a2613f4b2fb530f3ad112e36c4, 0x01000000000000000000000000000000),
(0xa606832c6f3c47a38b8fecca0c5bd0ca, 0x00000000000000000000000000000000),
(0x0d1f3992167c437ba4e1e8ce27b778c4, 0x00000000000000000000000000000000),
(0x847affdd9c764520aeb2472658dee3fd, 0x03000000000000000000000000000000),
(0x836aafe3aa694012a4b8a615cd62cde7, 0x04000000000000000000000000000000),
(0xeab6e47b37cb47888169f86a0dcf81cb, 0x05000000000000000000000000000000);