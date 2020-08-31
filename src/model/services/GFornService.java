package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.GFornDao;
import model.entities.GForn;

public class GFornService {

	private GFornDao dao = DaoFactory.createGFornDao();
	
	public List<GForn> findAll() {
		//MOCK
//		List<GForn> list = new ArrayList<>();
//		list.add(new GForn(1, 1137, "01/01/2019"));
//		list.add(new GForn(1, 1138, "02/01/2019"));
//		list.add(new GForn(1, 1139, "03/01/2019"));
//		return list;
		
		return dao.findAll();
		
	}
	
	public void saveOrUpdate(GForn obj) {
		
		if (obj.getPkGForn() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(GForn obj) {
		dao.deleteById(obj.getPkGForn());
	}
}
