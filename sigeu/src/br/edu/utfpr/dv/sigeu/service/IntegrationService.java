package br.edu.utfpr.dv.sigeu.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
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
import br.edu.utfpr.dv.sigeu.persistence.HibernateUtil;
import br.edu.utfpr.dv.sigeu.persistence.Transaction;
import br.edu.utfpr.dv.sigeu.sort.ClassroomComparator;
import br.edu.utfpr.dv.sigeu.sort.ReservaDataComparator;

import com.adamiworks.utils.StringUtils;
import com.adamiworks.utils.SystemOutUtils;
import com.adamiworks.utils.ldap.LdapUtils;

public class IntegrationService {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy");

	// private static SimpleDateFormat hourFormat = new
	// SimpleDateFormat("HH:mm");

	public static void deleteAllPreviousTimetables() throws Exception {
		Transaction transaction = null;

		try {
			transaction = new Transaction();
			transaction.begin();
			TimetableDAO ttDAO = new TimetableDAO(transaction);
			ttDAO.deleteAllPreviousTimetables(Config.getInstance().getCampus());
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
	public static void writeUploadFile(String fileName, byte[] data)
			throws IOException {
		String pathUpload = Config.getInstance().getConfig(
				Config.CONFIG_PATH_UPLOAD);
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
	 * Importa apenas os professores do XML
	 * 
	 * @param xmlFileName
	 * @throws Exception
	 */
	public static void importProfessoresXml(String xmlFileName)
			throws Exception {
		String fileName = Config.getInstance().getConfig(
				Config.CONFIG_PATH_UPLOAD)
				+ File.separator + xmlFileName;

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
		timetable.setIdCampus(Config.getInstance().getCampus());

		// Declaração de objetos
		NodeList nodeList = null;

		// Recupera Teachers
		List<Teacher> teacherList = new ArrayList<Teacher>();

		nodeList = doc.getElementsByTagName("teacher");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nNode;

				String id = e.getAttribute("id").trim();
				String name = e.getAttribute("name").trim();
				String shortname = StringUtils.left(e.getAttribute("short")
						.trim(), 32);

				if (name.length() == 0 || id.length() == 0
						|| shortname.length() == 0) {
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

		// Gravação
		Transaction trans = null;

		try {
			trans = new Transaction();
			trans.begin();

			List<Professor> listProfessor = new ArrayList<Professor>();
			ProfessorDAO professorDAO = new ProfessorDAO(trans);

			for (Teacher t : teacherList) {
				Professor p = professorDAO.encontrePorCodigo(Config
						.getInstance().getCampus(), t.getId());

				if (p == null) {
					p = new Professor();
				}

				p.setCodigo(StringUtils.left(t.getId().trim(), 32));
				p.setCor(StringUtils.left(t.getColor().trim(), 12));
				p.setGenero(t.getGender());
				p.setIdCampus(timetable.getIdCampus());
				p.setName(StringUtils.left(t.getName().trim(), 128));

				if (p.getIdProfessor() == null) {
					professorDAO.criar(p);
				} else {
					professorDAO.alterar(p);
				}

				listProfessor.add(p);
			}

			trans.commit();
		} catch (Exception e) {
			if (trans != null) {
				trans.close();
			}
		}
	}

	/**
	 * Importa os dados do arquivo XML para o banco de dados.
	 * 
	 * @param xmlFileName
	 * @throws Exception
	 */
	public static Integer importXml(String xmlFileName) throws Exception {
		// Remove todas as importações anteriores
		IntegrationService.deleteAllPreviousTimetables();

		String fileName = Config.getInstance().getConfig(
				Config.CONFIG_PATH_UPLOAD)
				+ File.separator + xmlFileName;

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
		timetable.setIdCampus(Config.getInstance().getCampus());

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
				String shortname = StringUtils.left(e.getAttribute("short")
						.trim(), 32);

				if (name.length() == 0 || id.length() == 0
						|| shortname.length() == 0) {
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
				String shortname = StringUtils.left(e.getAttribute("short")
						.trim(), 32);

				if (name.length() == 0 || id.length() == 0
						|| shortname.length() == 0) {
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
				String shortname = StringUtils.left(e.getAttribute("short")
						.trim(), 32);

				if (name.length() == 0 || id.length() == 0
						|| shortname.length() == 0) {
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

		nodeList = doc.getElementsByTagName("teacher");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nNode;

				String id = e.getAttribute("id").trim();
				String name = e.getAttribute("name").trim();
				String shortname = StringUtils.left(e.getAttribute("short")
						.trim(), 32);

				if (name.length() == 0 || id.length() == 0
						|| shortname.length() == 0) {
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

				Integer period = Integer.valueOf(e.getAttribute("period")
						.trim());
				// String name = e.getAttribute("name").trim();
				String name = period.toString();
				String shortname = StringUtils.left(e.getAttribute("short")
						.trim(), 32);

				if (period == null || name.length() == 0
						|| shortname.length() == 0) {
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

				if (id.length() == 0 || classids.length() == 0
						|| subjectids.length() == 0
						|| classroomids.length() == 0
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

				if (lessonid.length() == 0 || classroomids.length() == 0
						|| period.length() == 0 || days.length() == 0) {
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

			// Gravação
			timetableDAO.criar(timetable);

			for (Clazz clazz : clazzList) {
				clazzDAO.criar(clazz);
			}

			for (Subject subject : subjectList) {
				subjectDAO.criar(subject);
			}

			for (Teacher teacher : teacherList) {
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
	public static void criaCalendarioAula(Integer idTimeTable,
			Integer idPeriodoLetivo) throws Exception {
		Campus campus = Config.getInstance().getCampus();
		Transaction trans = null;

		try {
			trans = new Transaction();
			trans.begin();

			ReservaDAO reservaDAO = new ReservaDAO(trans);
			PeriodoLetivoDAO periodoLetivoDAO = new PeriodoLetivoDAO(trans);

			PeriodoLetivo periodoLetivo = periodoLetivoDAO
					.encontrePorId(idPeriodoLetivo);

			// Antes de começar, elimina todas as reservas feitas com a
			// transação anterior, se houver.
			Transacao transacao = periodoLetivo.getIdTransacaoReserva();
			if (transacao != null) {
				reservaDAO.removeByTransacao(campus, transacao);
			}

			trans.commit();
			trans.begin();
			// trans.close();
			// //////////////////////////////////////////////////////////////

			// trans = new Transaction();
			// trans.begin();

			TimetableDAO timetableDAO = new TimetableDAO(trans);
			ProfessorDAO professorDAO = new ProfessorDAO(trans);
			DisciplinaDAO disciplinaDAO = new DisciplinaDAO(trans);
			ClasseDAO classeDAO = new ClasseDAO(trans);
			// CalendarioAulaDAO calendarioAulaDAO = new
			// CalendarioAulaDAO(trans);
			LessonDAO lessonDAO = new LessonDAO(trans);
			PeriodDAO periodDAO = new PeriodDAO(trans);
			ItemReservaDAO itemReservaDAO = new ItemReservaDAO(trans);
			CategoriaItemReservaDAO categoriaItemReservaDAO = new CategoriaItemReservaDAO(
					trans);
			TipoReservaDAO tipoReservaDAO = new TipoReservaDAO(trans);
			// periodoLetivoDAO = new PeriodoLetivoDAO(trans);
			// PessoaDAO pessoaDAO = new PessoaDAO(trans);

			// Cria nova transação
			transacao = TransacaoService.criar("Importação XML ASC TimeTables");

			periodoLetivo.setIdTransacaoReserva(transacao);
			periodoLetivoDAO.alterar(periodoLetivo);

			trans.commit();
			trans.begin();

			// Recupera categoria de sala de aula
			CategoriaItemReserva salaDeAula = categoriaItemReservaDAO
					.encontrePorDescricao(campus, "SALA / AUDITÓRIO");

			// Recupera categoria de laboratório
			CategoriaItemReserva laboratorio = categoriaItemReservaDAO
					.encontrePorDescricao(campus, "LABORATÓRIO");

			// Recupera tipo de reserva "Aula Regular"
			TipoReserva tipoReserva = tipoReservaDAO.encontrePorDescricao(
					campus, "AULA REGULAR");

			// Responsável pelas reservas
			// Pessoa usuarioAdmin = pessoaDAO.encontrePorId(1);
			Pessoa usuarioAdmin = Config.getInstance().getPessoaLogin();

			if (salaDeAula == null) {
				throw new Exception(
						"Categoria 'SALA / AUDITÓRIO' não localizada");
			}

			if (laboratorio == null) {
				throw new Exception("Categoria 'LABORATÓRIO' não localizada");
			}

			if (tipoReserva == null) {
				throw new Exception(
						"Tipo de Reserva 'AULA REGULAR' não localizada");
			}

			if (usuarioAdmin == null) {
				throw new Exception("Pessoa 'ADMIN' não localizada");
			}

			Timetable timetable = timetableDAO.encontrePorId(idTimeTable);

			// Inicializa lista de salas de aula e cria/atualiza
			Hibernate.initialize(timetable.getClassroomList());
			List<Classroom> listClassroom = timetable.getClassroomList();
			List<ItemReserva> listSala = new ArrayList<ItemReserva>();

			listClassroom.sort(new ClassroomComparator());

			for (Classroom c : listClassroom) {
				CategoriaItemReserva categoria = null;

				if (c.getName().trim().toLowerCase().substring(0, 3)
						.equals("lab")) {
					categoria = laboratorio;
				} else {
					categoria = salaDeAula;
				}

				ItemReserva sala = itemReservaDAO
						.encontrePorDescricaoECategoria(campus, categoria,
								c.getName());

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

				if (sala.getIdItemReserva() == null) {
					itemReservaDAO.criar(sala);
				} else {
					itemReservaDAO.alterar(sala);
				}

				listSala.add(sala);
			}

			// Inicializa lista de Professores
			Hibernate.initialize(timetable.getTeacherList());
			List<Teacher> listTeacher = timetable.getTeacherList();
			List<Professor> listProfessor = new ArrayList<Professor>();

			for (Teacher t : listTeacher) {
				Professor p = professorDAO.encontrePorCodigo(campus, t.getId());

				if (p == null) {
					p = new Professor();
				}

				p.setCodigo(StringUtils.left(t.getId().trim(), 32));
				p.setCor(StringUtils.left(t.getColor().trim(), 12));
				p.setGenero(t.getGender());
				p.setIdCampus(timetable.getIdCampus());
				p.setName(StringUtils.left(t.getName().trim(), 128));

				if (p.getIdProfessor() == null) {
					professorDAO.criar(p);
				} else {
					professorDAO.alterar(p);
				}

				listProfessor.add(p);
			}

			// Inicializa a lista de Disciplinas
			Hibernate.initialize(timetable.getSubjectList());
			List<Subject> listSubject = timetable.getSubjectList();
			List<Disciplina> listDisciplina = new ArrayList<Disciplina>();

			for (Subject s : listSubject) {
				Disciplina d = disciplinaDAO.encontrePorCodigo(campus,
						s.getId());

				if (d == null) {
					d = new Disciplina();
				}

				d.setCodigo(StringUtils.left(s.getId(), 32));
				d.setIdCampus(campus);
				d.setNome(StringUtils.left(s.getName(), 64));
				d.setRotulo(StringUtils.left(s.getShortname().trim(), 12));

				// System.out.println(d);

				if (d.getIdDisciplina() == null) {
					disciplinaDAO.criar(d);
				} else {
					disciplinaDAO.alterar(d);
				}

				listDisciplina.add(d);
			}

			// Inicializa a lista de Classe
			Hibernate.initialize(timetable.getClazzList());
			List<Clazz> listClazz = timetable.getClazzList();
			List<Classe> listClasse = new ArrayList<Classe>();

			for (Clazz z : listClazz) {
				if (z.getName().trim().length() == 0) {
					continue;
				}

				Classe c = classeDAO.encontrePorCodigo(campus, z.getId());

				if (c == null) {
					c = new Classe();
				}

				c.setCodigo(StringUtils.left(z.getId().trim(), 32));
				c.setIdCampus(campus);
				c.setNome(StringUtils.left(z.getName().trim(), 32));
				c.setRotulo(StringUtils.left(z.getShortname().trim(), 12));

				if (c.getIdClasse() == null) {
					classeDAO.criar(c);
				} else {
					classeDAO.alterar(c);
				}

				listClasse.add(c);
			}

			trans.commit();
			trans.begin();
			// trans.close();

			// FASE 2 //

			// int total = 0;

			// trans = new Transaction();
			// trans.begin();

			// lessonDAO = new LessonDAO(trans);
			// periodDAO = new PeriodDAO(trans);

			Hibernate.initialize(timetable.getCardList());
			List<Card> listCard = timetable.getCardList();

			List<Reserva> listReserva = new ArrayList<Reserva>();

			for (Card card : listCard) {
				// total++;

				// Recupera o Lesson
				Lesson lesson = lessonDAO.encontrePorId(idTimeTable,
						card.getLessonid());

				String classes = "";

				// Recupera as classes do Lesson
				List<Classe> listClasseCal = new ArrayList<Classe>();
				String classids[] = lesson.getClassids().split(",");
				for (String s : classids) {
					for (Classe c : listClasse) {
						if (s.equals(c.getCodigo())) {
							listClasseCal.add(c);
							classes += c.getRotulo() + " ";
							continue;
						}
					}
				}

				String nomeUsuario = "";
				Professor professor = null;
				Pessoa usuario = null; // A reserva ficará em nome de um
										// professor apenas, independentemente
										// de ter sobreposição de horários de
										// mais de um professor em uma mesma
										// sala na mesma hora

				// Recupera professores do Lesson
				List<Professor> listProfessorCal = new ArrayList<Professor>();
				String teachers[] = lesson.getTeacherids().split(",");
				for (String s : teachers) {
					for (Professor p : listProfessor) {
						if (s.equals(p.getCodigo())) {

							nomeUsuario += p.getName() + ", ";

							// Encontra o registro de pessoa associado ao
							// Professor
							professor = professorDAO.encontrePorCodigo(campus,
									p.getCodigo());

							listProfessorCal.add(professor);

							if (professor.getProfessorPessoa() != null) {
								usuario = professor.getProfessorPessoa()
										.getIdPessoa();
							}

							continue;
						}
					}
				}

				nomeUsuario = StringUtils.cutLast(nomeUsuario, 2);

				// Recupera as salas registradas no card
				String classroomids[] = card.getClassroomids().split(",");

				// Navega entre as classroomid do Card
				// IGNORAR CLASSROOMIDS DE LESSON
				for (String classroomid : classroomids) {
					// Sala
					ItemReserva sala = null;
					for (ItemReserva ir : listSala) {
						if (ir.getCodigo().equals(classroomid)) {
							sala = ir;
							break;
						}
					}

					if (sala == null) {
						throw new Exception("Sala não encontrada [Lesson:"
								+ lesson.getId() + "][Classroomid:"
								+ classroomid + "]");
					}

					// Periodo
					Period period = periodDAO.encontrePorNome(idTimeTable,
							card.getPeriod());

					// Disciplina
					Disciplina disciplina = null;
					for (Disciplina d : listDisciplina) {
						if (d.getCodigo().equals(lesson.getSubjectids())) {
							disciplina = d;
							break;
						}
					}

					if (disciplina == null) {
						throw new Exception(
								"Disciplina não encontrada [Lesson:"
										+ lesson.getId() + "]");
					}

					// ====================================================================================================
					// Cria uma reserva para a sala de aula em cada dia da
					// semana até o fim do semestre
					// ====================================================================================================
					Calendar dia = Calendar.getInstance();
					dia.setTime(periodoLetivo.getDataInicio());

					// reservaDAO = new ReservaDAO(trans);

					// int count = 0;

					while (true) {
						// Somente se o dia da semana for compatível
						if (dia.get(Calendar.DAY_OF_WEEK) == DiaEnum
								.getDiaEnumById(card.getDays()).getDia()) {
							Reserva reserva = new Reserva();
							reserva.setImportado(true);
							reserva.setIdTransacao(transacao);
							reserva.setHoraGravacao(Calendar.getInstance()
									.getTime());
							reserva.setDataGravacao(Calendar.getInstance()
									.getTime());
							reserva.setData(dia.getTime());
							reserva.setHoraFim(period.getEndtime());
							reserva.setHoraInicio(period.getStarttime());
							reserva.setIdCampus(campus);
							reserva.setIdItemReserva(sala);
							reserva.setIdTipoReserva(tipoReserva);
							reserva.setIdTransacao(transacao);
							reserva.setIdUsuario(usuario);
							reserva.setNomeUsuario(nomeUsuario.trim()
									.toUpperCase());
							reserva.setIdPessoa(usuarioAdmin);
							reserva.setIdAutorizador(usuarioAdmin);
							reserva.setEmailNotificacao(usuarioAdmin.getEmail());
							reserva.setRotulo(StringUtils.left(classes.trim(),
									32));
							// reserva.setCor("#BBD2D2");
							reserva.setCor(professor.getCor());
							reserva.setStatus(StatusReserva.EFETIVADA
									.getStatus());

							StringBuilder motivo = new StringBuilder();
							// motivo.append(disciplina.getRotulo()).append("-").append(disciplina.getNome()).append("\n");
							motivo.append(disciplina.getRotulo()).append("-")
									.append(disciplina.getNome()).append("\n ");

							for (Classe c : listClasseCal) {
								// motivo.append(c.getRotulo()).append("-").append(c.getNome()).append("\n");
								motivo.append(c.getNome()).append("\n ");
							}

							reserva.setMotivo(motivo.toString());

							// SimpleDateFormat sdf = new SimpleDateFormat(
							// "dd/MM/yyyy");
							// SimpleDateFormat sdf2 = new SimpleDateFormat(
							// "HH:mm");
							//
							// System.out.println("--> Reserva: ["
							// + sdf.format(reserva.getData()) + " "
							// + sdf2.format(reserva.getHoraInicio())
							// + " "
							// + reserva.getIdItemReserva().getNome()
							// + "]");

							listReserva.add(reserva);

							// count++;
							//
							// if (count == 1000) {
							// trans.commit();
							// trans.begin();
							// count = 0;
							//
							// System.out.println(total
							// + " reservas gravadas.");
							// }
						}

						// Se forem os mesmos dias, encerra o laço
						if (dia.getTime().compareTo(periodoLetivo.getDataFim()) == 0) {
							break;
						}

						// Incrementa 1 dia
						dia.add(Calendar.DAY_OF_MONTH, 1);
					}

					// if (count > 0) {
					// trans.commit();
					// trans.begin();
					// }

				}
			}

			listReserva.sort(new ReservaDataComparator());

			int count = 0;

			Date dateShow = null;
			Date dateControl = null;

			for (Reserva r : listReserva) {
				count++;

				dateControl = r.getData();

				if (dateShow == null || !dateControl.equals(dateShow)) {
					dateShow = dateControl;
					System.out.println("Data: " + dateFormat.format(dateShow));
				}

				/** AQUI A RESERVA É CRIADA **/
				reservaDAO.criar(r);

				if (count == 50) {
					trans.commit();
					trans.begin();
					count = 0;

					// System.out.println("Data: "
					// + dateFormat.format(r.getData()) + " "
					// + hourFormat.format(r.getHoraInicio()) + "-"
					// + hourFormat.format(r.getHoraFim()));

				}

			}

			if (count > 0) {
				trans.commit();
			}

		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}

		// Atualiza professores
		IntegrationService.relacionaProfessorPessoa();
	}

	public static void relacionaProfessorPessoa() throws Exception {
		// Relaciona professores a Pessoa
		Campus campus = Config.getInstance().getCampus();

		Transaction trans = null;
		try {
			trans = new Transaction();
			trans.begin();

			ProfessorDAO professorDAO = new ProfessorDAO(trans);
			PessoaDAO pessoaDAO = new PessoaDAO(trans);
			ProfessorPessoaDAO professorPessoaDAO = new ProfessorPessoaDAO(
					trans);

			List<Pessoa> listPessoa = pessoaDAO.pesquisa(Config.getInstance()
					.getCampus(), null, 0);
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
					double sim = StringUtils.similarity(
							pessoa.getNomeCompleto(), prof.getName());

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
						System.out.println(prof.getName() + " ~ "
								+ ps.getPessoa().getNomeCompleto() + " = "
								+ ps.getDistance());
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
					pp = professorPessoaDAO
							.encontrePorId(prof.getIdProfessor());

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
					System.out.println(prof.getName()
							+ " NÃO POSSUI REGISTROS SEMELHANTES.");
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
	public static class PessoaSimilarityComparator implements
			Comparator<PessoaSimilarity> {

		@Override
		public int compare(PessoaSimilarity arg0, PessoaSimilarity arg1) {
			// Descending arg1 - arg0
			return new Double((arg1.getDistance() * 1000)
					- (arg0.getDistance() * 1000)).intValue();
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
	public static void atualizaPessoasLdap(Campus campus) throws Exception {
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

			LdapUtils ldapUtils = new LdapUtils(ldap.getHost(), ldap.getPort(),
					ldap.getSsl(), true, ldap.getBasedn(), ldap.getVarLdapUid());

			// ///////////////////////////////////
			List<String> mapa = ldapUtils.getAllLdapInfo(ldap.getVarLdapUid());
			/**
			 * Usar este código abaixo para encontrar todos os UIDs com os
			 * grupos em uma chamada ao LDAP só.
			 */
			Map<String, String> userTree = ldapUtils.getAllUsers(ldap
					.getVarLdapUid());

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

			SystemOutUtils logger = new SystemOutUtils(false);

			String varLdapCampus = ldap.getVarLdapCampus().trim().toUpperCase();

			for (String s : mapa) {
				logger.printTimestamp("1");

				attrs = s.split(LdapUtils.ENTRY_SEPARATOR);

				logger.printTimestamp("2");

				cnpjCpf = null;
				matricula = null;
				nomeCompleto = null;
				email = null;
				uid = null;
				lCampus = null;

				for (String a : attrs) {
					map = a.split(":");

					if (map[0].trim().toUpperCase()
							.equals(ldap.getVarLdapCnpjCpf().toUpperCase())) {
						cnpjCpf = map[1].trim();
					}

					if (map[0].trim().toUpperCase()
							.equals(ldap.getVarLdapEmail().toUpperCase())) {
						email = map[1].trim();
					}

					if (map[0].trim().toUpperCase()
							.equals(ldap.getVarLdapMatricula().toUpperCase())) {
						matricula = map[1].trim();
					}

					if (map[0]
							.trim()
							.toUpperCase()
							.equals(ldap.getVarLdapNomeCompleto().toUpperCase())) {
						nomeCompleto = map[1].trim();
					}

					if (map[0].trim().toUpperCase()
							.equals(ldap.getVarLdapUid().toUpperCase())) {
						uid = map[1].trim();
					}

					if (map[0].trim().toUpperCase()
							.equals(ldap.getVarLdapCampus().toUpperCase())) {
						lCampus = map[1].trim().toUpperCase();
					}
				}

				System.out.println(uid);

				System.out.println("---> Criadas: " + criadas
						+ " | Alteradas: " + alteradas + " | Ignorados: "
						+ ignorados);

				logger.printTimestamp("3");

				logger.printTimestamp("4");

				pessoa = null;

				/**
				 * Se for cadastro de aluno e do mesmo campus não exige e-mail.
				 * Se o e-mail for null, preenche com unknown@unknown.com
				 */
				if (uid.matches("[A-Z|a-z]{1}[0-9].*")) {
					if (nomeCompleto == null) {
						ignorados++;
						continue;
					}
					/**
					 * ALUNO
					 */
					// if (lCampus.equals(varLdapCampus)) {
					/**
					 * ALUNO DO MESMO CAMPUS
					 */
					if (email == null || email.trim().length() == 0) {
						email = "unknown@unknown.com";
					}
					// } else {
					// /**
					// * ALUNO DE OUTRO CAMPUS - IGNORAR
					// */
					// ignorados++;
					// continue;
					// }

					cnpjCpf = (cnpjCpf == null ? "00000000000" : cnpjCpf);
					matricula = (matricula == null ? "00000000" : matricula);
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

					if (cnpjCpf == null || matricula == null
							|| nomeCompleto == null) {
						ignorados++;
						continue;
					}
				}

				// Cadastra apenas pessoas do mesmo campus através da sigla de
				// identificação
				// Isto poderá remover alguns servidores que foram transferidos,
				// para tanto,
				// será necessário que eles façam login no sistema para ter o
				// cadastro realizado.
				// if (ldap.getIdCampus().getSigla() != null
				// && ldap.getIdCampus().getSigla().trim().length() > 0) {
				//
				// if (!ldap.getIdCampus().getSigla().toUpperCase().trim()
				// .equals(lCampus.toUpperCase())) {
				// continue;
				// }
				// }

				// pessoa = PessoaService.encontrePorEmail(email);
				pessoa = pessoaDAO.encontrePorEmail(email, campus);

				logger.printTimestamp("5");

				update = true;

				// Atualiza dados da Pessoa/Usuário
				if (pessoa == null) {
					logger.printTimestamp("5.1.1");
					pessoa = new Pessoa();
					update = false;
					pessoa.setAtivo(true);
					pessoa.setSenhaMd5("00000000000000000000000000000000");
					logger.printTimestamp("5.1.2");
				} else {
					logger.printTimestamp("5.2.1");
					fieldsIgnored = 0;

					if (pessoa.getCnpjCpf().trim().toUpperCase()
							.equals(cnpjCpf.trim().toUpperCase())) {
						fieldsIgnored++;
					}

					if (pessoa.getEmail().trim().toUpperCase()
							.equals(email.trim().toUpperCase())) {
						fieldsIgnored++;
					}

					if (pessoa.getMatricula().trim().toUpperCase()
							.equals(matricula.trim().toUpperCase())) {
						fieldsIgnored++;
					}

					if (pessoa.getNomeCompleto().trim().toUpperCase()
							.equals(nomeCompleto.trim().toUpperCase())) {
						fieldsIgnored++;
					}

					// Se todos os campos continuam iguais, não faz nada
					if (fieldsIgnored >= 4) {
						ignorados++;
						continue;
					}

					logger.printTimestamp("5.2.2");
				}

				pessoa.setCnpjCpf(cnpjCpf);
				pessoa.setEmail(email);
				pessoa.setIdCampus(ldap.getIdCampus());
				pessoa.setMatricula(matricula);
				pessoa.setNomeCompleto(nomeCompleto);
				pessoa.setPessoaFisica(true);

				if (update) {
					logger.printTimestamp("6.1.1");
					// PessoaService.alterar(pessoa);
					pessoaDAO.alterar(pessoa);
					alteradas++;
					logger.printTimestamp("6.1.2");
				} else {
					logger.printTimestamp("6.2.1");
					// PessoaService.criar(pessoa);
					pessoaDAO.criar(pessoa);
					criadas++;
					logger.printTimestamp("6.2.2");
				}

				commitCount++;

				logger.printTimestamp("7");

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

				logger.printTimestamp("8");

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
						commitCount = 0;

						// Abre outra transação
						trans.begin();

						// Recupera id do grupo
						gp = grupoPessoaDAO
								.encontrePorId(gp.getIdGrupoPessoa());
					}

					grupos.add(gp);
				}

				logger.printTimestamp("9");

				// Atualiza os grupos da pessoa
				GrupoPessoaService.atualizaGrupos(trans, pessoa, grupos);

				logger.printTimestamp("10");

				if (commitCount >= HibernateUtil.HIBERNATE_BATCH_SIZE) {
					commitCount = 0;
					trans.commit();
					trans.begin();
				}

				logger.printTimestamp("11");
			}

			if (commitCount > 0) {
				trans.commit();
				// trans.close();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (trans != null) {
				trans.close();
			}
		}
	}
}
