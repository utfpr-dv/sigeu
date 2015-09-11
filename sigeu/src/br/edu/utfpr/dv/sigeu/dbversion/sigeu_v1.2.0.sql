UPDATE public.parametro SET valor = '1.2.0' WHERE codigo = 'versao_database';

alter table public.item_reserva add numero_horas_antecedencia integer not null default 0;