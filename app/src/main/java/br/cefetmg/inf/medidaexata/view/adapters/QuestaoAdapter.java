package br.cefetmg.inf.medidaexata.view.adapters;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cefetmg.inf.android.medidaexata.activities.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;

import br.cefetmg.inf.medidaexata.model.QuestaoFechada;
import br.cefetmg.inf.medidaexata.view.IAlteraProgressBar;
import br.cefetmg.inf.medidaexata.view.IOnSemResultados;
import br.cefetmg.inf.medidaexata.view.dialogs.SemResultadosDialog;
import br.cefetmg.inf.medidaexata.view.fragments.QuestoesFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestaoAdapter
        extends FirestoreRecyclerAdapter<QuestaoFechada, QuestaoAdapter.QuestaoHolder> {

    // TAG usada para Log
    private final static String TAG = QuestaoAdapter.class.getSimpleName();
    // Listener de interações com os ViewHolders deste adapter
    private final QuestoesFragment.OnQuestaoInteractionListener frgListener;
    // Listener para executar alterações na ProgressBar indicadora de Loading
    private final IAlteraProgressBar altPbListener;
    // Listener para mostrar o Dialog Sem Resultados
    private final IOnSemResultados semResultadosListener;

    public QuestaoAdapter(FirestoreRecyclerOptions<QuestaoFechada> options,
                          QuestoesFragment.OnQuestaoInteractionListener frgListener,
                          IAlteraProgressBar altPbListener,
                          IOnSemResultados semResultadosListener) {
        super(options);
        this.frgListener = frgListener;
        this.altPbListener = altPbListener;
        this.semResultadosListener = semResultadosListener;
    }


    @Override
    public void onDataChanged() {
        super.onDataChanged();

        if(getItemCount() == 0) {
            altPbListener.escondeProgressBar();
            DialogFragment df = SemResultadosDialog.newInstance(SemResultadosDialog.SEM_QUESTOES);
            semResultadosListener.onSemQuestoes(df);
        }
    }

    @Override
    public QuestaoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        altPbListener.escondeProgressBar();

        View v = LayoutInflater
                .from(
                        parent.getContext())
                .inflate(
                        R.layout.list_item_questao,
                        parent,
                        false);

        return new QuestaoHolder(v);
    }

    @Override
    protected void onBindViewHolder(final QuestaoHolder qstHolder,
                                    int posicao,
                                    final QuestaoFechada qst) {
        // Seta cor mais clara ao enunciado de cada questão
        qstHolder.refTvEnunciado.setText(qst.getEnunciado().get(0));

        // Seta cor mais clara ao objeto de conhecimento
        qstHolder.refTvObjCon.setText(qst.getMateriaAbordada());

        // Adiciona Listeners para cada botão
        if(frgListener != null) {
            qstHolder.refBtVerQuestao.setOnClickListener(v ->
                    frgListener.onVerQuestaoInteraction(qst));
            qstHolder.refBtVerMateria.setOnClickListener(v ->
                    frgListener.onVerMateriaInteraction(qst));
        }
    }

    public class QuestaoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_enunciado) TextView refTvEnunciado;
        @BindView(R.id.tv_nome_materia_abordada) TextView refTvObjCon;
        @BindView(R.id.bt_ver_questao) MaterialButton refBtVerQuestao;
        @BindView(R.id.bt_ver_materia) MaterialButton refBtVerMateria;

        QuestaoHolder(View v) {
            super(v);
            try {
                ButterKnife.bind(this, v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + refTvObjCon.getText() + "'";
        }
    }
}
