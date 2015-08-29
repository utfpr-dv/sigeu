SELECT
    item.*
FROM
    public.item_reserva item JOIN
    public.categoria_item_reserva categoria ON
        item.id_categoria = categoria.id_categoria JOIN
    public.campus ON
        item.id_campus = campus.id_campus AND
        categoria.id_campus = campus.id_campus
WHERE
		item.ativo = true
    AND campus.id_campus = :idCampus
    AND categoria.id_categoria = :idCategoria
    AND (item.id_item_reserva = :idItemReserva OR :idItemReserva = 0)
    AND NOT EXISTS(
        SELECT
            reserva2.*
        FROM
            public.reserva reserva2 JOIN
            public.item_reserva item2 ON
                reserva2.id_item_reserva = item2.id_item_reserva JOIN
            public.categoria_item_reserva categoria2 ON
                item2.id_categoria = categoria2.id_categoria JOIN
            public.campus campus2 ON
                item2.id_campus = campus2.id_campus AND
                categoria2.id_campus = campus2.id_campus
        WHERE
               reserva2.data = :data
           AND reserva2.status = 'E'
           AND reserva2.id_campus = campus.id_campus
           AND reserva2.id_item_reserva = item.id_item_reserva
            AND (
                ( :horaInicio >= reserva2.hora_inicio AND :horaInicio < reserva2.hora_fim ) OR
                ( :horaFim > reserva2.hora_inicio AND :horaFim <= reserva2.hora_fim ) OR
                ( :horaInicio <= reserva2.hora_inicio AND :horaFim > reserva2.hora_fim ) OR
                ( :horaInicio > reserva2.hora_inicio AND :horaFim <= reserva2.hora_fim )
                )
    )
ORDER BY
	item.nome ASC