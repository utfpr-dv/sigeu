-- v.1.3.5
UPDATE public.parametro SET valor = '1.3.5' WHERE codigo = 'versao_database';

ALTER TABLE public.item_reserva ADD valida_periodo_letivo BOOLEAN NOT NULL DEFAULT TRUE;

