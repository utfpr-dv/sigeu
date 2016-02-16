package br.edu.utfpr.dv.sigeu.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.Hibernate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.edu.utfpr.dv.sigeu.config.Config;
import br.edu.utfpr.dv.sigeu.dao.CardDAO;
import br.edu.utfpr.dv.sigeu.dao.CategoriaItemReservaDAO;
import br.edu.utfpr.dv.sigeu.dao.ClasseDAO;
import br.edu.utfpr.dv.sigeu.dao.ClassroomDAO;
import br.edu.utfpr.dv.sigeu.dao.ClazzDAO;
import br.edu.utfpr.dv.sigeu.dao.DisciplinaDAO;
import br.edu.utfpr.dv.sigeu.dao.GrupoPessoaDAO;
import br.edu.utfpr.dv.sigeu.dao.ItemReservaDAO;
import br.edu.utfpr.dv.sigeu.dao.LessonDAO;
import br.edu.utfpr.dv.sigeu.dao.PeriodDAO;
import br.edu.utfpr.dv.sigeu.dao.PeriodoLetivoDAO;
import br.edu.utfpr.dv.sigeu.dao.PessoaDAO;
import br.edu.utfpr.dv.sigeu.dao.ProfessorDAO;
import br.edu.utfpr.dv.sigeu.dao.ProfessorPessoaDAO;
import br.edu.utfpr.dv.sigeu.dao.ReservaDAO;
import br.edu.utfpr.dv.sigeu.dao.SubjectDAO;
import br.edu.utfpr.dv.sigeu.dao.TeacherDAO;
import br.edu.utfpr.dv.sigeu.dao.TimetableDAO;
import br.edu.utfpr.dv.sigeu.dao.TipoReservaDAO;
import br.edu.utfpr.dv.sigeu.entities.Campus;
import br.edu.utfpr.dv.sigeu.entities.Card;
import br.edu.utfpr.dv.sigeu.entities.CategoriaItemReserva;
import br.edu.utfpr.dv.sigeu.entities.Classe;
import br.edu.utfpr.dv.sigeu.entities.Classroom;
import br.edu.utfpr.dv.sigeu.entities.Clazz;
import br.edu.utfpr.dv.sigeu.entities.Disciplina;
import br.edu.utfpr.dv.sigeu.entities.GrupoPessoa;
import br.edu.utfpr.dv.sigeu.entities.ItemReserva;
import br.edu.utfpr.dv.sigeu.entities.LdapServer;
import br.edu.utfpr.dv.sigeu.entities.Lesson;
import br.edu.utfpr.dv.sigeu.entities.Period;
import br.edu.utfpr.dv.sigeu.entities.PeriodoLetivo;
import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.entities.Professor;
import br.edu.utfpr.dv.sigeu.entities.ProfessorPessoa;
import br.edu.utfpr.dv.sigeu.entities.Reserva;
import br.edu.utfpr.dv.sigeu.entities.Subject;
import br.edu.utfpr.dv.sigeu.entities.Teacher;
import br.edu.utfpr.dv.sigeu.entities.Timetable;
import br.edu.utfpr.dv.sigeu.entities.TipoReserva;
import br.edu.utfpr.dv.sigeu.entities.Transacao;
import br.edu.utfpr.dv.sigeu.enumeration.DiaEnum;
import br.edu.utfpr.dv.sigeu.enumeration.StatusReserva;
import br.edu.utfpr.dv.sigeu.jsfbeans.ReservaAdminBean;
import br.edu.utfpr.dv.sigeu.maplist.LessonIdMapList;
import br.edu.utfpr.dv.sigeu.maplist.PeriodNameMapList;
import br.edu.utfpr.dv.sigeu.persistence.HibernateUtil;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;
import br.edu.utfpr.dv.sigeu.sort.ClassroomComparator;

import com.adamiworks.utils.DateTimeUtils;
import com.adamiworks.utils.StringUtils;
import com.adamiworks.utils.ldap.LdapUtils;

public class IntegrationService {

	public static final String PROFESSOR_NAO_CADASTRADO_NOME = "PROFESSOR NÃO CADASTRADO";
	private static final String PROFESSOR_NAO_CADASTRADO_ID = "0000000000000000";

	// private static SimpleDateFormat dateFormat = new SimpleDateFormat(
	// "dd/MM/yyyy");

	// private static SimpleDateFormat hourFormat = new
	// SimpleDateFormat("HH:mm");

	public static void deleteAllPreviousTimetables(Campus campus) throws Exception {
		Transaction transaction = null;

		try {
			transaction = new Transaction();
			transaction.begin();
			TimetableDAO ttDAO = new TimetableDAO(transaction);
			ttDAO.deleteAllPreviousTimetables(campus);
			transaction.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			transaction.close();
		}
	}

	/**
	 * Grava um arquivo em disco retornando o objeto File correspondente.
	 * 
	 * @param fileName
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static void writeUploadFile(String fileName, byte[] data) throws IOException {

		String pathUpload = null;

		// if (Config.getInstance().isDebugMode()) {
		// pathUpload = Config.getInstance().getConfig(
		// "dev." + Config.CONFIG_PATH_UPLOAD);
		// } else {
		pathUpload = Config.getInstance().getConfig(Config.CONFIG_PATH_UPLOAD);
		// }

		fileName = pathUpload + File.separator + fileName;

		File file = new File(fileName);

		if (file.exists()) {
			file.delete();
		}

		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		fos.close();
	}

	/**
	 * Importa os dados do arquivo XML para o banco de dados.
	 * 
	 * @param xmlFileName
	 * @throws Exception
	 */
	public static Integer importXml(Campus campus, String xmlFileName) throws Exception {
		// Remove todas as importações anteriores
		IntegrationService.deleteAllPreviousTimetables(campus);

		// String fileName = Config.getInstance().getConfig(
		// (Config.getInstance().isDebugMode() ? "dev." : "")
		// + Config.CONFIG_PATH_UPLOAD)
		// + File.separator + xmlFileName;

		String fileName = Config.getInstance().getConfig(Config.CONFIG_PATH_UPLOAD) + File.separator + xmlFileName;

		System.out.println("Importando arquivo XML: " + fileName);

		File xmlFile = new File(fileName);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);

		// Necessário??
		doc.getDocumentElement().normalize();

		// Criando objeto principal de importação
		Timetable timetable = new Timetable();
		timetable.setNomeArquivo(xmlFile.getName());
		timetable.setDataCarregamento(Calendar.getInstance().getTime());
		timetable.setIdCampus(campus);

		// Declaração de objetos
		NodeList nodeList = null;

		// 1 - Recupera Classes
		List<Clazz> clazzList = new ArrayList<Clazz>();

		nodeList = doc.getElementsByTagName("class");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);

			// Está no atributo class
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nNode;

				String id = e.getAttribute("id").trim();
				String name = e.getAttribute("name").trim();
				String shortname = StringUtils.left(e.getAttribute("short").trim(), 32);

				if (name.length() == 0 || id.length() == 0 || shortname.length() == 0) {
					continue;
				}

				Clazz c = new Clazz();
				c.setId(id);
				c.setName(name);
				c.setShortname(shortname);
				c.setIdTimetable(timetable);

				clazzList.add(c);
			}

		}

		// 2 - Recupera Subjects
		List<Subject> subjectList = new ArrayList<Subject>();

		nodeList = doc.getElementsByTagName("subject");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nNode;

				String id = e.getAttribute("id").trim();
				String name = e.getAttribute("name").trim();
				String shortname = StringUtils.left(e.getAttribute("short").trim(), 32);

				if (name.length() == 0 || id.length() == 0 || shortname.length() == 0) {
					continue;
				}

				Subject c = new Subject();
				c.setId(id);
				c.setName(name);
				c.setShortname(shortname);
				c.setIdTimetable(timetable);

				subjectList.add(c);
			}
		}

		// 3 - Recupera Classrooms
		List<Classroom> classroomList = new ArrayList<Classroom>();

		nodeList = doc.getElementsByTagName("classroom");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nNode;

				String id = e.getAttribute("id").trim();
				String name = e.getAttribute("name").trim();
				String shortname = StringUtils.left(e.getAttribute("short").trim(), 32);

				if (name.length() == 0 || id.length() == 0 || shortname.length() == 0) {
					continue;
				}

				Classroom c = new Classroom();
				c.setId(id);
				c.setName(name);
				c.setShortname(shortname);
				c.setIdTimetable(timetable);

				classroomList.add(c);
			}
		}

		// 4 - Recupera Teachers
		List<Teacher> teacherList = new ArrayList<Teacher>();

		// -------------------------------------
		// Add professor nao cadastrado --------
		Teacher pnc = new Teacher();
		pnc.setId(PROFESSOR_NAO_CADASTRADO_ID);
		pnc.setName(PROFESSOR_NAO_CADASTRADO_NOME);
		pnc.setShortname(PROFESSOR_NAO_CADASTRADO_NOME);
		pnc.setGender('M');
		pnc.setColor("#BABACA");
		pnc.setIdTimetable(timetable);

		teacherList.add(pnc);
		// -------------------------------------

		nodeList = doc.getElementsByTagName("teacher");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nNode;

				String id = e.getAttribute("id").trim();
				String name = e.getAttribute("name").trim();
				String shortname = StringUtils.left(e.getAttribute("short").trim(), 32);

				if (name.length() == 0 || id.length() == 0 || shortname.length() == 0) {
					continue;
				}

				Teacher c = new Teacher();
				c.setId(id);
				c.setName(name);
				c.setShortname(shortname);
				c.setGender(e.getAttribute("gender").charAt(0));
				c.setColor(e.getAttribute("color"));
				c.setIdTimetable(timetable);

				teacherList.add(c);
			}
		}

		// 5 - Recupera Period
		List<Period> periodList = new ArrayList<Period>();

		nodeList = doc.getElementsByTagName("period");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nNode;

				Integer period = Integer.valueOf(e.getAttribute("period").trim());
				// String name = e.getAttribute("name").trim();
				String name = period.toString();
				String shortname = StringUtils.left(e.getAttribute("short").trim(), 32);

				if (period == null || name.length() == 0 || shortname.length() == 0) {
					continue;
				}

				// VERIFICAR SE JÁ TEM UM PERÍODO COM MESMO NOME
				boolean exists = false;
				for (Period p : periodList) {
					if (p.getOrdem() == period) {
						exists = true;
						continue;
					}
				}

				if (exists) {
					continue;
				}

				Period c = new Period();
				c.setIdPeriod(period);
				c.setName(name);
				c.setShortname(shortname);
				c.setOrdem(Integer.valueOf(period));

				//
				String start = e.getAttribute("starttime");
				String end = e.getAttribute("endtime");

				int startHour = Integer.valueOf(start.split(":")[0]);
				int startMinute = Integer.valueOf(start.split(":")[1]);
				int endHour = Integer.valueOf(end.split(":")[0]);
				int endMinute = Integer.valueOf(end.split(":")[1]);

				Calendar starttime = Calendar.getInstance();
				starttime.set(Calendar.HOUR_OF_DAY, startHour);
				starttime.set(Calendar.MINUTE, startMinute);
				starttime.set(Calendar.SECOND, 0);
				starttime.set(Calendar.MILLISECOND, 0);

				Calendar endtime = Calendar.getInstance();
				endtime.set(Calendar.HOUR_OF_DAY, endHour);
				endtime.set(Calendar.MINUTE, endMinute);
				endtime.set(Calendar.SECOND, 0);
				endtime.set(Calendar.MILLISECOND, 0);

				c.setStarttime(starttime.getTime());
				c.setEndtime(endtime.getTime());
				//

				c.setIdTimetable(timetable);

				periodList.add(c);
			}
		}

		// 6 - Recupera Lesson
		List<Lesson> lessonList = new ArrayList<Lesson>();

		nodeList = doc.getElementsByTagName("lesson");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nNode;

				String id = e.getAttribute("id").trim();
				String classids = e.getAttribute("classids").trim();
				String subjectids = e.getAttribute("subjectid").trim();
				String classroomids = e.getAttribute("classroomids").trim();
				String teacherids = e.getAttribute("teacherids").trim();

				// if (id.equals("7850B82A3ED95253")) {
				// System.out.println(id);
				// }

				teacherids = (teacherids == null || teacherids.trim().length() == 0 ? PROFESSOR_NAO_CADASTRADO_ID
						: teacherids);

				if (id.length() == 0 || classids.length() == 0 || subjectids.length() == 0 || classroomids.length() == 0
						|| teacherids.length() == 0) {
					continue;
				}

				Lesson c = new Lesson();
				c.setId(id);
				c.setClassids(classids);
				c.setClassroomids(classroomids);
				c.setSubjectids(subjectids);
				c.setTeacherids(teacherids);
				c.setIdTimetable(timetable);

				lessonList.add(c);
			}
		}

		// 7 - Recupera Card
		List<Card> cardList = new ArrayList<Card>();

		nodeList = doc.getElementsByTagName("card");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nNode;

				String lessonid = e.getAttribute("lessonid").trim();
				String classroomids = e.getAttribute("classroomids").trim();
				String period = e.getAttribute("period").trim();
				String days = e.getAttribute("days").trim();

				if (lessonid.length() == 0 || classroomids.length() == 0 || period.length() == 0
						|| days.length() == 0) {
					continue;
				}

				Card c = new Card();
				c.setLessonid(lessonid);
				c.setClassroomids(classroomids);
				c.setPeriod(period);
				c.setDays(days);
				c.setIdTimetable(timetable);

				cardList.add(c);
			}
		}

		// Inicia gravação de objetos
		Transaction trans = new Transaction();

		try {
			trans.begin();

			// Declaração de DAOs
			TimetableDAO timetableDAO = new TimetableDAO(trans);
			ClazzDAO clazzDAO = new ClazzDAO(trans);
			SubjectDAO subjectDAO = new SubjectDAO(trans);
			TeacherDAO teacherDAO = new TeacherDAO(trans);
			ClassroomDAO classroomDAO = new ClassroomDAO(trans);
			PeriodDAO periodDAO = new PeriodDAO(trans);
			LessonDAO lessonDAO = new LessonDAO(trans);
			CardDAO cardDAO = new CardDAO(trans);
			ProfessorDAO professorDAO = new ProfessorDAO(trans);

			// Gravação
			timetableDAO.criar(timetable);

			for (Clazz clazz : clazzList) {
				clazzDAO.criar(clazz);
			}

			for (Subject subject : subjectList) {
				subjectDAO.criar(subject);
			}

			for (Teacher teacher : teacherList) {

				Professor p = professorDAO.encontrePorCodigo(campus, teacher.getId());

				if (p == null) {
					p = new Professor();
				}

				p.setCodigo(StringUtils.left(teacher.getId().trim().toUpperCase(), 32));
				p.setCor(StringUtils.left(teacher.getColor().trim(), 12));
				p.setGenero(teacher.getGender());
				p.setIdCampus(timetable.getIdCampus());
				p.setName(StringUtils.left(teacher.getName().trim(), 128).trim().toUpperCase());

				if (p.getIdProfessor() == null) {
					professorDAO.criar(p);
				} else {
					professorDAO.alterar(p);
				}

				teacherDAO.criar(teacher);
			}

			for (Classroom classroom : classroomList) {
				classroomDAO.criar(classroom);
			}

			for (Period period : periodList) {
				periodDAO.criar(period);
			}

			for (Lesson lesson : lessonList) {
				lessonDAO.criar(lesson);
			}

			for (Card card : cardList) {
				cardDAO.criar(card);
			}

			trans.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}

		// Importação final
		// criaCalendarioAula(timetable.getIdTimetable());

		// Relaciona professores
		System.out.println("Relaciona Professor Pessoa...");
		IntegrationService.relacionaProfessorPessoa(campus);
		System.out.println("Relaciona Professor Pessoa... OK");

		return timetable.getIdTimetable();

	}

	/**
	 * Importa os dados gravados do último XML e atualiza o cadastro de
	 * Professores, Disciplinas, Cursos, Classes, Aulas, Periodo Letivo e
	 * Calendário de Aula.
	 * 
	 * @param idTimeTable
	 *            Código do timetable referente à importação do XML.
	 * @param idPeriodoLetivo
	 *            Código do Periodo letivo selecionado na importação do XML
	 * @throws Exception
	 */
	public static void geraReservasDoXml(Campus campus, Pessoa pessoaLogin, ReservaAdminBean bean, Integer idTimeTable,
			Integer idPeriodoLetivo) throws Exception {

		// Atualiza professores
		// IntegrationService.relacionaProfessorPessoa();

		Transaction trans = null;

		try {
			System.out.println("Iniciando transação...");
			trans = new Transaction();
			trans.begin();
			System.out.println("Iniciando transação...OK");

			ReservaDAO reservaDAO = new ReservaDAO(trans);
			PeriodoLetivoDAO periodoLetivoDAO = new PeriodoLetivoDAO(trans);

			PeriodoLetivo periodoLetivo = periodoLetivoDAO.encontrePorId(idPeriodoLetivo);

			System.out.println("Eliminando registros antigos...");

			// Antes de começar, elimina todas as reservas feitas com a
			// transação anterior, se houver.
			Transacao transacao = periodoLetivo.getIdTransacaoReserva();
			if (transacao != null) {
				reservaDAO.removeByTransacao(campus, transacao);
			}

			trans.commit();
			System.out.println("Eliminando registros antigos...OK");

			System.out.println("Reiniciando transação...");
			trans.begin();
			// //////////////////////////////////////////////////////////////

			TimetableDAO timetableDAO = new TimetableDAO(trans);
			ProfessorDAO professorDAO = new ProfessorDAO(trans);
			DisciplinaDAO disciplinaDAO = new DisciplinaDAO(trans);
			ClasseDAO classeDAO = new ClasseDAO(trans);
			LessonDAO lessonDAO = new LessonDAO(trans);
			PeriodDAO periodDAO = new PeriodDAO(trans);
			ItemReservaDAO itemReservaDAO = new ItemReservaDAO(trans);
			CategoriaItemReservaDAO categoriaItemReservaDAO = new CategoriaItemReservaDAO(trans);
			TipoReservaDAO tipoReservaDAO = new TipoReservaDAO(trans);

			System.out.println("Eliminando registros antigos...OK");

			System.out.println("Criando novo transacao do SIGEU...");

			// Cria nova transação
			transacao = TransacaoService.criar(campus, pessoaLogin, "Importação XML ASC TimeTables");

			periodoLetivo.setIdTransacaoReserva(transacao);
			periodoLetivoDAO.alterar(periodoLetivo);

			trans.commit();
			System.out.println("Criando novo transacao do SIGEU...OK");

			trans.begin();

			System.out.println("Recuperando registros padrão...");

			// Recupera categoria de sala de aula
			CategoriaItemReserva salaDeAula = categoriaItemReservaDAO.encontrePorDescricao(campus,
					"SALA / MINI-AUDITÓRIO");

			// Recupera categoria de laboratório
			CategoriaItemReserva laboratorio = categoriaItemReservaDAO.encontrePorDescricao(campus, "LABORATÓRIO");

			// Recupera tipo de reserva "Aula Regular"
			TipoReserva tipoReserva = tipoReservaDAO.encontrePorDescricao(campus, "AULA REGULAR");

			// Responsável pelas reservas
			// Pessoa usuarioAdmin = pessoaDAO.encontrePorId(1);
			Pessoa usuarioAdmin = pessoaLogin;

			if (salaDeAula == null) {
				throw new Exception("Categoria 'SALA / AUDITÓRIO' não localizada");
			}

			if (laboratorio == null) {
				throw new Exception("Categoria 'LABORATÓRIO' não localizada");
			}

			if (tipoReserva == null) {
				throw new Exception("Tipo de Reserva 'AULA REGULAR' não localizada");
			}

			if (usuarioAdmin == null) {
				throw new Exception("Pessoa 'ADMIN' não localizada");
			}

			System.out.println("Recuperando registros padrão...OK");

			Timetable timetable = timetableDAO.encontrePorId(idTimeTable);

			// Inicializa lista de salas de aula e cria/atualiza
			Hibernate.initialize(timetable.getClassroomList());
			List<Classroom> listClassroom = timetable.getClassroomList();
			listClassroom.sort(new ClassroomComparator());

			// === TRATAMENTO DE LISTA DE SALA ===
			System.out.println("Classroom looping...");

			Map<String, ItemReserva> mapListSala = new HashMap<String, ItemReserva>();

			for (Classroom c : listClassroom) {
				CategoriaItemReserva categoria = null;

				if (c.getName().trim().toLowerCase().substring(0, 3).equals("lab")) {
					categoria = laboratorio;
				} else {
					categoria = salaDeAula;
				}

				if (c.getShortname().trim().toUpperCase().equals("B4-S4")) {
					System.out.println(c.getShortname());
				}

				/*
				 * ALTERAÇÃO REALIZADA PARA RESPEITAR O ÍNDICE DE UNICIDADE
				 * "ak_item_reserva_nome" DO BANCO DE DADOS. DESTA FORMA NÃO SE
				 * ALTERA A CATEGORIA DA SALA DE AULA (OU LABORATÓRIOS) SE ELES
				 * JÁ EXISTIREM.
				 */
				// ItemReserva sala =
				// itemReservaDAO.encontrePorDescricaoECategoria(campus,
				// categoria, c.getName());
				ItemReserva sala = itemReservaDAO.encontrePorDescricao(campus, c.getName());

				if (sala == null) {
					sala = itemReservaDAO.encontrePorRotulo(campus, c.getShortname());
				}

				if (sala == null) {
					sala = new ItemReserva();
				}

				sala.setAtivo(true);
				sala.setDetalhes("Importado por aSc TimeTables");
				sala.setIdCampus(campus);
				sala.setIdCategoria(categoria);
				sala.setNome(c.getName());
				sala.setCodigo(c.getId());
				sala.setPatrimonio("N/A");
				sala.setRotulo(c.getShortname());
				sala.setNumeroHorasAntecedencia(0);

				if (sala.getIdItemReserva() == null) {
					itemReservaDAO.criar(sala);
				} else {
					itemReservaDAO.alterar(sala);
				}

				mapListSala.put(sala.getCodigo().trim().toUpperCase(), sala);
			}

			listClassroom = null;

			// === FIM TRATAMENTO DE LISTA DE SALA ===

			System.out.println("Classroom looping...OK");

			// Inicializa lista de Professores
			Hibernate.initialize(timetable.getTeacherList());

			/*
			 * Tratamento de lista e Map de Teacher
			 */
			System.out.println("Teacher looping...");

			List<Teacher> listTeacherTmp = timetable.getTeacherList();
			Map<String, Professor> mapListProfessor = new HashMap<String, Professor>();

			for (Teacher t : listTeacherTmp) {
				Professor p = professorDAO.encontrePorCodigo(campus, t.getId());
				// Mapa pelo código do Teacher
				mapListProfessor.put(p.getCodigo().trim().toUpperCase(), p);
			}

			System.out.println("Teacher looping...OK");

			// Zera a lita de Teacher
			listTeacherTmp = null;

			System.out.println("Professor looping...OK");

			System.out.println("Subject looping...");

			// Inicializa a lista de Disciplinas
			Hibernate.initialize(timetable.getSubjectList());
			List<Subject> listSubject = timetable.getSubjectList();
			Map<String, Disciplina> mapListDisciplina = new HashMap<String, Disciplina>();

			for (Subject s : listSubject) {
				Disciplina d = disciplinaDAO.encontrePorCodigo(campus, s.getId());

				if (d == null) {
					d = new Disciplina();
				}

				d.setCodigo(StringUtils.left(s.getId(), 32));
				d.setIdCampus(campus);
				d.setNome(StringUtils.left(s.getName(), 64));
				d.setRotulo(StringUtils.left(s.getShortname().trim(), 12));

				if (d.getIdDisciplina() == null) {
					disciplinaDAO.criar(d);
				} else {
					disciplinaDAO.alterar(d);
				}

				mapListDisciplina.put(d.getCodigo(), d);
			}

			listSubject = null;

			System.out.println("Subject looping...OK");

			System.out.println("Clazz looping...");

			// Inicializa a lista de Classe
			Hibernate.initialize(timetable.getClazzList());
			List<Clazz> listClazz = timetable.getClazzList();
			Map<String, Classe> mapListClasse = new HashMap<String, Classe>();

			for (Clazz clazz : listClazz) {
				if (clazz.getName().trim().length() == 0) {
					continue;
				}

				Classe c = classeDAO.encontrePorCodigo(campus, clazz.getId());

				if (c == null) {
					c = new Classe();
				}

				c.setCodigo(StringUtils.left(clazz.getId().trim(), 32));
				c.setIdCampus(campus);
				c.setNome(StringUtils.left(clazz.getName().trim(), 32));
				c.setRotulo(StringUtils.left(clazz.getShortname().trim(), 12));

				if (c.getIdClasse() == null) {
					classeDAO.criar(c);
				} else {
					classeDAO.alterar(c);
				}

				mapListClasse.put(c.getCodigo(), c);
			}

			System.out.println("Clazz looping...OK");

			System.out.println("COMMIT...");

			trans.commit();

			System.out.println("COMMIT...OK");

			System.out.println("Carrega Lessons...");

			LessonIdMapList liml = new LessonIdMapList(lessonDAO.encontrePorTimeTable(idTimeTable));
			Map<Object, Lesson> mapListLesson = liml.getMap();

			System.out.println("Carrega Lessons...OK");

			System.out.println("Carrega Period...");

			List<Period> listPeriod = periodDAO.getAll(campus);
			PeriodNameMapList pnl = new PeriodNameMapList(listPeriod);
			Map<Object, Period> mapPeriodList = pnl.getMap();

			System.out.println("Carrega Period...OK");

			trans.begin();
			reservaDAO = new ReservaDAO(trans);
			int count = 0;

			Hibernate.initialize(timetable.getCardList());
			List<Card> listCard = timetable.getCardList();

			Calendar dia = Calendar.getInstance();
			dia.setTime(periodoLetivo.getDataInicio());

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			System.out.println("PeriodoLetivo Looping...");

			// Total de dias entre o intervalo
			int totalDias = DateTimeUtils.dateDiff(periodoLetivo.getDataInicio(), periodoLetivo.getDataFim()).intValue()
					+ 1;

			/**
			 * LOOPING FINAL ONDE AS RESERVAS SÃO CRIADAS E GRAVADAS DENTRO DO
			 * PERÍODO ENTRE DATA INICIAL E FINAL DO PERÍODO
			 */
			BigDecimal total = new BigDecimal(String.valueOf(totalDias));
			BigDecimal row = new BigDecimal("0");
			int progress = 0;

			// while (true) {
			for (int i = 0; i <= totalDias; i++) {

				// Adds a line
				row = new BigDecimal(String.valueOf(i));

				progress = row.divide(total, 2, RoundingMode.HALF_DOWN).multiply(new BigDecimal("100")).intValue();

				bean.setProgress(progress);

				System.out.println("Data: " + sdf.format(dia.getTime()) + "  progresso: " + progress + "%");

				if (!FeriadoService.verificaFeriado(campus, dia.getTime())) {

					if (dia.getTime().compareTo(periodoLetivo.getDataFim()) > 0) {
						System.out.println("Período letivo encerrado");
						break;
					}

					for (Card card : listCard) {
						// Somente se o dia da semana for compatível
						if (dia.get(Calendar.DAY_OF_WEEK) == DiaEnum.getDiaEnumById(card.getDays()).getDia()) {

							// Recupera o Lesson
							Lesson lesson = mapListLesson.get(card.getLessonid());

							if (lesson == null || lesson.getClassids() == null) {
								System.out.println("ID_CARD=" + card.getIdCard() + " LESSONID=" + card.getLessonid()
										+ " CLASSROOMIDS=" + card.getClassroomids());
							}

							// Recupera as classes do Lesson
							Classe classe = null;

							String classids[] = lesson.getClassids().split(",");
							for (String classid : classids) {
								classe = mapListClasse.get(classid);

								String nomeUsuario = "";
								Pessoa usuario = null;

								// === Recupera professores do Lesson ===
								String teachers[] = lesson.getTeacherids().split(",");

								for (String s : teachers) {

									Professor p = mapListProfessor.get(s.toUpperCase().trim());

									if (p == null) {
										p = mapListProfessor.get(PROFESSOR_NAO_CADASTRADO_ID);
									}

									nomeUsuario = p.getName();

									if (p.getProfessorPessoa() != null) {
										usuario = p.getProfessorPessoa().getIdPessoa();
									}

									// QUANDO O USUÁRIO NÃO FOR IDENTIFICADO,
									// DEFINE-SE O USUARIO LOGADO
									if (usuario == null) {
										usuario = pessoaLogin;
									}

									// Recupera as salas registradas no card
									String classroomids[] = card.getClassroomids().split(",");

									// Navega entre as classroomid do Card
									// IGNORAR CLASSROOMIDS DE LESSON
									for (String classroomid : classroomids) {
										ItemReserva sala = mapListSala.get(classroomid);

										if (sala == null) {
											throw new Exception("Sala não encontrada [Lesson:" + lesson.getId()
													+ "][Classroomid:" + classroomid + "]");
										}

										// Periodo
										// Period period =
										// periodDAO.encontrePorNome(
										// idTimeTable, card.getPeriod());
										Period period = mapPeriodList.get(card.getPeriod());

										// Disciplina
										Disciplina disciplina = mapListDisciplina.get(lesson.getSubjectids());

										if (disciplina == null) {
											throw new Exception(
													"Disciplina não encontrada [Lesson:" + lesson.getId() + "]");
										}

										StringBuilder motivo = new StringBuilder();

										motivo.append("[DISCIPLINA ").append(disciplina.getRotulo()).append(" - ")
												.append(disciplina.getNome()).append("] / [TURMA ")
												.append(classe.getNome()).append("]");

										Reserva reserva = new Reserva();
										reserva.setImportado(true);
										reserva.setIdTransacao(transacao);
										reserva.setHoraGravacao(Calendar.getInstance().getTime());
										reserva.setDataGravacao(Calendar.getInstance().getTime());
										reserva.setData(dia.getTime());
										reserva.setHoraFim(period.getEndtime());
										reserva.setHoraInicio(period.getStarttime());
										reserva.setIdCampus(campus);
										reserva.setIdItemReserva(sala);
										reserva.setIdTipoReserva(tipoReserva);
										reserva.setIdTransacao(transacao);
										reserva.setIdUsuario(usuario);
										reserva.setNomeUsuario(nomeUsuario.trim().toUpperCase());
										reserva.setIdPessoa(usuarioAdmin);
										reserva.setIdAutorizador(usuarioAdmin);
										reserva.setEmailNotificacao(usuarioAdmin.getEmail());
										reserva.setRotulo(StringUtils.left(classe.toString(), 32));
										reserva.setCor(p.getCor() == null ? "#BBD2D2" : p.getCor());
										reserva.setStatus(StatusReserva.EFETIVADA.getStatus());

										reserva.setMotivo(motivo.toString());

										reservaDAO.criar(reserva);

										count++;

										if (count >= HibernateUtil.HIBERNATE_BATCH_SIZE) {
											trans.commit();
											trans.begin();
											count = 0;

										}
									}
								}
							}
						}
					}

				} else {
					System.out.println("=== Feriado");
				}

				// Incrementa um dia
				dia.add(Calendar.DAY_OF_MONTH, 1);
			}

			if (count > 0) {
				trans.commit();
				System.out.println(total + " reservas gravadas.");
			}

			System.out.println("PeriodoLetivo Looping...OK");

		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}

	}

	public static void relacionaProfessorPessoa(Campus campus) throws Exception {
		// Relaciona professores a Pessoa
		Transaction trans = null;
		try {
			trans = new Transaction();
			trans.begin();

			ProfessorDAO professorDAO = new ProfessorDAO(trans);
			PessoaDAO pessoaDAO = new PessoaDAO(trans);
			ProfessorPessoaDAO professorPessoaDAO = new ProfessorPessoaDAO(trans);

			List<Pessoa> listPessoa = pessoaDAO.pesquisa(campus, null, 0);
			// List<Pessoa> listPessoa = pessoaDAO.pesquisaPorGrupo(Config
			// .getInstance().getCampus(), "PROFESSORES", 0);
			List<Professor> listProfessor = professorDAO.pesquisaTodos(campus);

			List<PessoaSimilarity> listSimilarity = null;

			for (Professor prof : listProfessor) {
				listSimilarity = new ArrayList<IntegrationService.PessoaSimilarity>();

				/**
				 * Encontra a similaridade entre os nomes e ordena pela mais
				 * provável. Ignora taxas menores que 80%, pois só deve
				 * considerar pequenos erros de grafia de acentuação ou falta de
				 * preposições.
				 */
				for (Pessoa pessoa : listPessoa) {
					double sim = StringUtils.similarity(pessoa.getNomeCompleto(), prof.getName());

					if (sim >= 0.80d) {
						PessoaSimilarity pps = new PessoaSimilarity();
						pps.setPessoa(pessoa);
						pps.setDistance(sim);
						listSimilarity.add(pps);
					}
				}

				if (listSimilarity.size() > 0) {
					/**
					 * Ordena a lista
					 */
					listSimilarity.sort(new PessoaSimilarityComparator());

					for (PessoaSimilarity ps : listSimilarity) {
						System.out.println(
								prof.getName() + " ~ " + ps.getPessoa().getNomeCompleto() + " = " + ps.getDistance());
					}

					System.out.println("===");

					Pessoa pessoa = listSimilarity.get(0).getPessoa();

					// for (Pessoa pessoa : listPessoa) {
					// if (prof.getName()
					// .trim()
					// .toUpperCase()
					// .equals(pessoa.getNomeCompleto().trim()
					// .toUpperCase())) {
					boolean exists = true;

					ProfessorPessoa pp = null;
					pp = professorPessoaDAO.encontrePorId(prof.getIdProfessor());

					if (pp == null) {
						exists = false;
						pp = new ProfessorPessoa();
						pp.setIdProfessor(prof.getIdProfessor());
						pp.setProfessor(prof);
					}

					pp.setIdPessoa(pessoa);

					if (!exists) {
						professorPessoaDAO.criar(pp);
					} else {
						professorPessoaDAO.alterar(pp);
					}

				} else {
					System.out.println(prof.getName() + " NÃO POSSUI REGISTROS SEMELHANTES.");
					// }
				}
			}

			trans.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}
	}

	/**
	 * Classe para armazenar similaridade entre nomes de professores e pessoas.
	 * 
	 * @author Tiago
	 *
	 */
	private static class PessoaSimilarity {

		private Pessoa pessoa;
		private double distance;

		Pessoa getPessoa() {
			return pessoa;
		}

		void setPessoa(Pessoa pessoa) {
			this.pessoa = pessoa;
		}

		double getDistance() {
			return distance;
		}

		void setDistance(double distance) {
			this.distance = distance;
		}

	}

	/**
	 * Ordena a lista pelo valor de proximidade.
	 * 
	 * @author Tiago
	 *
	 */
	public static class PessoaSimilarityComparator implements Comparator<PessoaSimilarity> {

		@Override
		public int compare(PessoaSimilarity arg0, PessoaSimilarity arg1) {
			// Descending arg1 - arg0
			return new Double((arg1.getDistance() * 1000) - (arg0.getDistance() * 1000)).intValue();
		}

	}

	/**
	 * Lê toda a estrutura de cadastros do servidor LDAP e cadastra todas as
	 * pessoas na base de dados
	 * 
	 * @param campus
	 *            Campus a ser atualizado
	 * @throws Exception
	 */
	public static void atualizaPessoasLdap(ReservaAdminBean bean, Campus campus) throws Exception {
		Date inicio = Calendar.getInstance().getTime();
		Date fim = null;

		int commitCount = 0;

		Transaction trans = null;

		try {
			trans = new Transaction();
			// Campus campus = Config.getInstance().getCampus();

			trans.begin();

			PessoaDAO pessoaDAO = new PessoaDAO(trans);
			GrupoPessoaDAO grupoPessoaDAO = new GrupoPessoaDAO(trans);

			// LdapServer ldap = LdapServerService.encontrePorEmail(emailLogin);
			LdapServer ldap = campus.getLdapServerList().get(0);

			LdapUtils ldapUtils = new LdapUtils(ldap.getHost(), ldap.getPort(), ldap.getSsl(), true, ldap.getBasedn(),
					ldap.getVarLdapUid());

			// ///////////////////////////////////
			List<String> mapa = ldapUtils.getAllLdapInfo(ldap.getVarLdapUid());
			/**
			 * Usar este código abaixo para encontrar todos os UIDs com os
			 * grupos em uma chamada ao LDAP só.
			 */
			Map<String, String> userTree = ldapUtils.getAllUsers(ldap.getVarLdapUid());

			int criadas = 0;
			int alteradas = 0;
			int ignorados = 0;

			/* Declaração de variáveis do loop */
			String attrs[] = null;
			String cnpjCpf = null;
			String matricula = null;
			String nomeCompleto = null;
			String email = null;
			String uid = null;
			String lCampus = null;
			String map[] = null;
			Pessoa pessoa = null;
			boolean update = true;
			String baseDn = null;
			int fieldsIgnored = 0;
			List<GrupoPessoa> grupos = null;
			List<String> nomeGrupos = null;
			GrupoPessoa gp = null;
			/**/

			// String varLdapCampus =
			// ldap.getVarLdapCampus().trim().toUpperCase();

			BigDecimal total = new BigDecimal(String.valueOf(mapa.size()));
			BigDecimal row = new BigDecimal("0");
			int progress = 0;

			for (String s : mapa) {
				// Adds a line
				row = row.add(new BigDecimal("1"));

				progress = row.divide(total, 2, RoundingMode.HALF_DOWN).multiply(new BigDecimal("100")).intValue();

				bean.setProgress(progress);

				attrs = s.split(LdapUtils.ENTRY_SEPARATOR);

				cnpjCpf = null;
				matricula = null;
				nomeCompleto = null;
				email = null;
				uid = null;
				lCampus = null;

				for (String a : attrs) {
					map = a.split(":");

					if (map[0].trim().toUpperCase().equals(ldap.getVarLdapCnpjCpf().toUpperCase())) {
						cnpjCpf = map[1].trim();
					}

					if (map[0].trim().toUpperCase().equals(ldap.getVarLdapEmail().toUpperCase())) {
						email = map[1].trim();
					}

					if (map[0].trim().toUpperCase().equals(ldap.getVarLdapMatricula().toUpperCase())) {
						matricula = map[1].trim();
					}

					if (map[0].trim().toUpperCase().equals(ldap.getVarLdapNomeCompleto().toUpperCase())) {
						nomeCompleto = map[1].trim();
					}

					if (map[0].trim().toUpperCase().equals(ldap.getVarLdapUid().toUpperCase())) {
						uid = map[1].trim();
					}

					if (map[0].trim().toUpperCase().equals(ldap.getVarLdapCampus().toUpperCase())) {
						lCampus = map[1].trim().toUpperCase();
					}
				}

				// System.out.println(uid);

				pessoa = null;

				/**
				 * Se for cadastro de aluno ignora
				 */
				if (email == null || email.trim().length() == 0
						|| !email.toLowerCase().contains(ldap.getSufixoEmail())) {
					ignorados++;
					continue;
				} else {
					/**
					 * SERVIDOR - TAE OU PROFESSOR
					 */
					if (email == null || email.trim().length() == 0) {
						// Restringe cadastro apenas a servidores que possuem
						// e-mail
						ignorados++;
						continue;
					}

					if (cnpjCpf == null || matricula == null || nomeCompleto == null) {
						ignorados++;
						continue;
					}
				}

				// pessoa = PessoaService.encontrePorEmail(email);
				pessoa = pessoaDAO.encontrePorEmail(email, campus);

				update = true;

				// Atualiza dados da Pessoa/Usuário
				if (pessoa == null) {
					pessoa = new Pessoa();
					update = false;
					pessoa.setAtivo(true);
					pessoa.setSenhaMd5("00000000000000000000000000000000");
				} else {
					fieldsIgnored = 0;

					if (pessoa.getCnpjCpf().trim().toUpperCase().equals(cnpjCpf.trim().toUpperCase())) {
						fieldsIgnored++;
					}

					if (pessoa.getEmail().trim().toUpperCase().equals(email.trim().toUpperCase())) {
						fieldsIgnored++;
					}

					if (pessoa.getMatricula().trim().toUpperCase().equals(matricula.trim().toUpperCase())) {
						fieldsIgnored++;
					}

					if (pessoa.getNomeCompleto().trim().toUpperCase().equals(nomeCompleto.trim().toUpperCase())) {
						fieldsIgnored++;
					}

					if (pessoa.getLdapCampus().trim().toUpperCase().equals(lCampus.trim().toUpperCase())) {
						fieldsIgnored++;
					}

					// Se todos os campos continuam iguais, não faz nada
					if (fieldsIgnored >= 5) {
						ignorados++;
						continue;
					}
				}

				pessoa.setCnpjCpf(cnpjCpf);
				pessoa.setEmail(email);
				pessoa.setIdCampus(ldap.getIdCampus());
				pessoa.setMatricula(matricula);
				pessoa.setNomeCompleto(nomeCompleto);
				pessoa.setPessoaFisica(true);
				pessoa.setLdapCampus(lCampus);

				if (update) {
					// PessoaService.alterar(pessoa);
					pessoaDAO.alterar(pessoa);
					alteradas++;
				} else {
					// PessoaService.criar(pessoa);
					pessoaDAO.criar(pessoa);
					criadas++;
				}

				commitCount++;

				/**
				 * ======================================================
				 * ENCONTRA GRUPO DA PESSOA
				 * ======================================================
				 */
				// Confere os grupos da Pessoa
				// baseDn = ldapUtils.getDnByUid(uid);

				// Pesquisa o uid atual dentro da lista da árvore
				baseDn = userTree.get(uid);

				if (baseDn == null) {
					continue;
				}

				grupos = new ArrayList<GrupoPessoa>();
				nomeGrupos = ldapUtils.getLdapOuByUid(uid, baseDn);
				/**
				 * ======================================================
				 */

				for (String grupo : nomeGrupos) {
					// GrupoPessoa gp =
					// GrupoPessoaService.encontrePorDescricao(grupo);
					gp = grupoPessoaDAO.encontrePorDescricao(campus, grupo);

					if (gp == null) {
						gp = new GrupoPessoa();
						gp.setIdCampus(ldap.getIdCampus());
						gp.setNome(grupo);

						grupoPessoaDAO.criar(gp);

						// Grava grupo
						trans.commit();
						// commitCount = 0;

						// Abre outra transação
						trans.begin();

						// Recupera id do grupo
						gp = grupoPessoaDAO.encontrePorId(gp.getIdGrupoPessoa());
					}

					grupos.add(gp);
				}

				// Atualiza os grupos da pessoa
				GrupoPessoaService.atualizaGrupos(trans, pessoa, grupos);

				if (commitCount >= HibernateUtil.HIBERNATE_BATCH_SIZE) {

					System.out.println(commitCount + " registros processados.");
					System.out.println(
							"---> Criadas: " + criadas + " | Alteradas: " + alteradas + " | Ignorados: " + ignorados);

					commitCount = 0;
					trans.commit();
					trans.begin();
				}
			}

			if (commitCount > 0) {
				trans.commit();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (trans != null) {
				trans.close();
			}
		}

		fim = Calendar.getInstance().getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		System.out.println("============================================");
		System.out.println("Início: " + sdf.format(inicio));
		System.out.println("Fim...: " + sdf.format(fim));

	}
}
