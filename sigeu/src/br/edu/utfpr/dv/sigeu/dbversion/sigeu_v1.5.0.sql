DROP VIEW reserva_view;

ALTER TABLE item_reserva ALTER COLUMN nome SET DATA TYPE text;

CREATE OR REPLACE VIEW reserva_view AS 
 SELECT r.id_reserva,
    r.id_transacao,
    date_part('dow'::text, r.data) AS dia_semana,
    r.data,
    r.hora_inicio,
    r.hora_fim,
    r.id_tipo_reserva,
    t.descricao AS tipo_reserva,
    r.id_item_reserva,
    i.nome AS nome_item_reserva,
    r.id_pessoa,
    p.nome_completo AS nome_pessoa,
    p.email AS email_pessoa,
    r.id_autorizador,
    a.nome_completo AS nome_autorizador,
    a.email AS email_autorizador,
    r.id_usuario,
    r.nome_usuario,
    r.email_notificacao,
    r.motivo,
    r.rotulo,
    r.cor,
    r.status,
    r.motivo_cancelamento,
    r.importado,
    i.patrimonio,
    r.data_gravacao,
    r.hora_gravacao,
    r.id_campus
   FROM reserva r
     JOIN tipo_reserva t ON t.id_tipo_reserva = r.id_tipo_reserva
     JOIN item_reserva i ON i.id_item_reserva = r.id_item_reserva
     JOIN pessoa p ON p.id_pessoa = r.id_pessoa
     JOIN pessoa a ON a.id_pessoa = r.id_autorizador;

ALTER TABLE reserva_view
  OWNER TO sigeu;
