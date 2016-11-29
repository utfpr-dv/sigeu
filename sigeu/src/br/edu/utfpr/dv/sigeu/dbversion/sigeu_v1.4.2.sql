UPDATE public.parametro SET valor = '1.4.2' WHERE codigo = 'versao_database';

--SELECT * FROM pessoa WHERE email LIKE '%monica%'
CREATE OR REPLACE FUNCTION public.tf_cadastro_pessoa_ldap_campus()
 RETURNS trigger
 LANGUAGE plpgsql
AS $BODY$
BEGIN
	IF RTRIM(COALESCE(NEW.ldap_campus,''))='' THEN
		NEW.ldap_campus = 'DV';
	END IF;
	RETURN NEW;
END;
$BODY$
`
COMMIT
`
DROP TRIGGER tr_cadastro_pessoa_ldap_campus_bi ON public.pessoa`
CREATE TRIGGER tr_cadastro_pessoa_ldap_campus_bi
BEFORE INSERT
ON public.pessoa
FOR EACH ROW
EXECUTE PROCEDURE public.tf_cadastro_pessoa_ldap_campus()`
COMMIT`
DROP TRIGGER tr_cadastro_pessoa_ldap_campus_bu ON public.pessoa`
CREATE TRIGGER tr_cadastro_pessoa_ldap_campus_bu
BEFORE UPDATE
ON public.pessoa
FOR EACH ROW
EXECUTE PROCEDURE public.tf_cadastro_pessoa_ldap_campus()`
COMMIT`