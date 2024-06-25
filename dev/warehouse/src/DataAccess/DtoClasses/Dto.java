
package DataAccess.DtoClasses;
        import DataAccess.ControllerClasses.Controller;

        import java.sql.SQLException;
        import java.util.*;
public abstract class Dto{
    protected boolean isPersisted;
    private Controller controller;
    public Dto(Controller controller,boolean fromDB)
    {
        isPersisted = fromDB;
        this.controller = controller;
    }
    public abstract void persist();

    /// <summary>
///// Inserts a new record into the database.
///// </summary>
///// <param name="attributesValues">The attribute values for the record.</param>
    protected void insert(Object[] attributesValues)
    {
        if(!controller.insert(attributesValues))
        {
            throw new IllegalArgumentException("An unexpected error occurred while insert");
        }
    }

    /// <summary>
/// Updates a record in the database.
/// </summary>
/// <param name="identifiersValues">The identifier values for the record.</param>
/// <param name="varToUpdate">The variable to update.</param>
/// <param name="valueToUpdate">The new value for the variable.</param>
    protected void update(Object[] identifiersValues,String varToUpdate,Object valueToUpdate)
    {
        if(!controller.update(identifiersValues,varToUpdate,valueToUpdate))
        {
            throw new IllegalArgumentException("An unexpected error occurred while update");
        }
    }

    /// <summary>
/// Deletes a record from the database.
/// </summary>
/// <param name="identifiersValues">The identifier values for the record.</param>
    protected void delete(Object[] identifiersValues) {
        if (!controller.delete(identifiersValues)) {
            throw new IllegalArgumentException("An unexpected error occurred while delete");
        }
    }

}

