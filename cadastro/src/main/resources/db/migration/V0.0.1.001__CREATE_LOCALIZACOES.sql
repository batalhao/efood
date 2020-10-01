-- DROP TABLE public.localizacoes;

CREATE TABLE public.localizacoes (
	id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	latitude float8 NULL,
	longitude float8 NULL,
	CONSTRAINT localizacoes_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE public.localizacoes OWNER TO efood_cadastro;
GRANT ALL ON TABLE public.localizacoes TO efood_cadastro;
